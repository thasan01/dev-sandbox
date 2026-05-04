const fs = require('fs');
const onvif = require('node-onvif');

(async ()=> {

  // Create an OnvifDevice object
  const device = new onvif.OnvifDevice({
    xaddr: 'http://CAMERA_HOSTNAME:8000/onvif/device_service',
    user: 'user',
    pass: 'pass'
  });

  // Initialize the OnvifDevice object
  const info = await device.init();
  // Call the async function
  console.log(JSON.stringify(info, null, '  '));

  let url = device.getUdpStreamUrl();
  console.log(url);

  //res = await device.fetchSnapshot()
  //fs.writeFileSync('snapshot.jpg', res.body, {encoding: 'binary'});  

})()
