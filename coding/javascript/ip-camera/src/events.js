/**
 * NodeJS ONVIF Events (Async/Await Version)
 */

const { Cam } = require('onvif');
const http = require('http');
const util = require('util');

// Configuration
const HOSTNAME = 'camera_hostname';
const PORT = 80;
const USERNAME = 'user';
const PASSWORD = 'pass';
const EVENT_RECEIVER_IP_ADDRESS = '192.168.1.179';
const EVENT_RECEIVER_PORT = 8086;

const EventMethodTypes = { PULL: "pull", SUBSCRIBE: "subscribe" };

// PICK WHICH EVENT METHOD TO USE
//const EVENT_MODE = EventMethodTypes.PULL;
const EVENT_MODE = EventMethodTypes.SUBSCRIBE;

// --- Helper Functions to "Promisify" the old Callback logic ---

// 1. Wrap the Camera Connection
const connectCamera = (config) => {
    return new Promise((resolve, reject) => {
        new Cam(config, function (err) {
            if (err) return reject(err);
            resolve(this); // 'this' is the connected camera instance
        });
    });
};

// 2. Start HTTP Server (Only for Subscribe Mode)
const startHttpServer = (camObj) => {
    return new Promise((resolve, reject) => {
        const server = http.createServer((request, response) => {
            let body = '';
            request.on('data', chunk => body += chunk);
            request.on('end', () => {
                if (request.method === "POST") {
                    // console.log('HTTP POST received'); 
                    response.writeHead(200, { "Content-Type": "text/plain" });
                    response.end("received POST request.");

                    if (camObj) {
                        camObj.parseEventXML(body, (err, data) => {
                            if (!err) ReceivedEvent(data);
                        });
                    }
                } else {
                    response.writeHead(200, { "Content-Type": "text/plain" });
                    response.end("Undefined request.");
                }
            });
        });

        server.listen(EVENT_RECEIVER_PORT, () => {
            console.log("Server running on port " + EVENT_RECEIVER_PORT);
            resolve(server);
        });
        server.on('error', (err) => reject(err));
    });
};


// --- Main Async Execution Flow ---

(async function main() {
    console.log("*******************************************************************************");
    console.log(`** Starting in mode: ${EVENT_MODE}`);
    console.log("*******************************************************************************");

    let cam_obj = null;

    try {
        // Step 1: Connect to Camera
        cam_obj = await connectCamera({
            hostname: HOSTNAME,
            username: USERNAME,
            password: PASSWORD,
            port: PORT,
            timeout: 10000,
            preserveAddress: true
        });
        console.log('Connected to ONVIF Device');

        // Step 2: Start Server if in Subscribe Mode
        // We pass cam_obj so the server knows how to decode incoming XML
        if (EVENT_MODE === EventMethodTypes.SUBSCRIBE) {
            await startHttpServer(cam_obj);
            console.log(`** The camera will be told to send events to ${EVENT_RECEIVER_IP_ADDRESS}:${EVENT_RECEIVER_PORT}`);
        } else {
            console.log("** The library will poll for events using a WS-Pull Point Subscription");
        }

        // Step 3: Promisify Camera Methods
        // We bind 'cam_obj' to ensure 'this' context is preserved
        const getDeviceInfo = util.promisify(cam_obj.getDeviceInformation).bind(cam_obj);
        const getSystemDate = util.promisify(cam_obj.getSystemDateAndTime).bind(cam_obj);
        const getCapabilities = util.promisify(cam_obj.getCapabilities).bind(cam_obj);
        const getEventProperties = util.promisify(cam_obj.getEventProperties).bind(cam_obj);
        const subscribe = util.promisify(cam_obj.subscribe).bind(cam_obj);

        // Step 4: Execute ONVIF Commands sequentially (Replacing flow.series)
        
        // A. Get Info
        const info = await getDeviceInfo();
        console.log('Manufacturer  ' + info.manufacturer);
        console.log('Model         ' + info.model);
        console.log('Firmware      ' + info.firmwareVersion);
        console.log('Serial Number ' + info.serialNumber);

        // B. Get Time
        const date = await getSystemDate();
        console.log('Device Time   ' + date);

        // C. Get Capabilities
        const caps = await getCapabilities();
        let hasEvents = false;
        if (caps.events) hasEvents = true;
        console.log("hasEvents: ", hasEvents);

        // D. Get Event Properties (Topic Discovery)
        let hasTopics = false;
        if (hasEvents) {
            const eventProps = await getEventProperties();
            
            // Helper to parse the nested topic tree
            const parseNode = (node, topicPath) => {
                for (const child in node) {
                    if (child === "$" || child === "messageDescription") {
                        if (child === "messageDescription") {
                            let source = node[child].source ? JSON.stringify(node[child].source) : '';
                            let data = node[child].data ? JSON.stringify(node[child].data) : '';
                            console.log('Found Event - ' + topicPath.toUpperCase());
                            if (source) console.log('  Source=' + source);
                            if (data) console.log('  Data=' + data);
                            hasTopics = true;
                            return;
                        }
                    } else {
                        parseNode(node[child], topicPath + '/' + child);
                    }
                }
            };
            
            if (eventProps.topicSet) {
                parseNode(eventProps.topicSet, '');
            }
            console.log('\n');
        }

        // Step 5: Activate Events (Pull or Subscribe)
        if (hasEvents && hasTopics) {
            if (EVENT_MODE === EventMethodTypes.SUBSCRIBE) {
                let uniqueID = 1001;
                let receiveUrl = `http://${EVENT_RECEIVER_IP_ADDRESS}:${EVENT_RECEIVER_PORT}/events/${uniqueID}`;
                
                await subscribe({ url: receiveUrl });
                console.log('Subscribed to events successfully. callback_url=', receiveUrl);
            } 
            else if (EVENT_MODE === EventMethodTypes.PULL) {
                // For PullPoint, we don't await a function, we just register the event listener
                cam_obj.on('event', (camMessage, xml) => {
                    ReceivedEvent(camMessage);
                });
                console.log('Registered PULL event listener.');

                // 🌟 ADD THIS BLOCK: Keep the script alive for PULL mode
                await new Promise(() => {
                    setInterval(() => {
                        // This empty timer keeps the Node.js process running
                        // while the ONVIF library handles the actual event polling.
                        process.stdout.write('.'); // Optional: show a sign of life
                    }, 30000); // 30 seconds interval
                });
                // 🌟 END OF ADDED BLOCK                
            }
        }

    } catch (err) {
        console.error("An error occurred during setup:", err);
    }

})();


// --- Event Processing Logic (Remains mostly pure) ---

function stripNamespaces(topic) {
    return topic.split('/')
        .map(part => part.split(':').pop())
        .join('/');
}

function ReceivedEvent(camMessage) {
    console.log("entered ReceivedEvent:", JSON.stringify(camMessage));
    // Basic validation to prevent crashes if message structure is unexpected
    if (!camMessage.topic || !camMessage.message) return;

    let eventTopic = stripNamespaces(camMessage.topic._);
    let eventTime = camMessage.message.message.$.UtcTime;
    let eventProperty = camMessage.message.message.$.PropertyOperation;

    let sourceName = null, sourceValue = null;
    let dataName = null, dataValue = null;

    // Extract Source
    const sourceObj = camMessage.message.message.source;
    if (sourceObj && sourceObj.simpleItem) {
        const item = Array.isArray(sourceObj.simpleItem) ? sourceObj.simpleItem[0] : sourceObj.simpleItem;
        sourceName = item.$.Name;
        sourceValue = item.$.Value;
    }

    // Extract Data
    const dataObj = camMessage.message.message.data;
    if (dataObj) {
        if (dataObj.simpleItem) {
            const items = Array.isArray(dataObj.simpleItem) ? dataObj.simpleItem : [dataObj.simpleItem];
            items.forEach(item => {
                processEvent(eventTime, eventTopic, eventProperty, sourceName, sourceValue, item.$.Name, item.$.Value);
            });
            return; // Return early as we processed the loop
        } else if (dataObj.elementItem) {
            dataName = 'elementItem';
            dataValue = JSON.stringify(dataObj.elementItem);
        }
    }

    processEvent(eventTime, eventTopic, eventProperty, sourceName, sourceValue, dataName, dataValue);
}

function processEvent(eventTime, eventTopic, eventProperty, sourceName, sourceValue, dataName, dataValue) {
    let output = `EVENT: ${typeof eventTime === 'object' ? eventTime.toJSON() : eventTime} ${eventTopic}`;
    if (eventProperty) output += ` PROP:${eventProperty}`;
    if (sourceName && sourceValue) output += ` SRC:${sourceName}=${sourceValue}`;
    if (dataName && dataValue) output += ` DATA:${dataName}=${dataValue}`;
    console.log(output);
}