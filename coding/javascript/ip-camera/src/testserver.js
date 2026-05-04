(async() => {
    const http = require('http');
    const EVENT_RECEIVER_PORT = 5000;

    const startHttpServer = () => {
        return new Promise((resolve, reject) => {
            const server = http.createServer((request, response) => {
                let body = '';
                request.on('data', chunk => body += chunk);
                request.on('end', () => {
                        response.writeHead(200, { "Content-Type": "text/plain" });
                        response.end("hello");
                });
            });

            server.listen(EVENT_RECEIVER_PORT, () => {
                console.log("Server running on port " + EVENT_RECEIVER_PORT);
                resolve(server);
            });
            server.on('error', (err) => reject(err));
        });
    };

    await startHttpServer();

})();
