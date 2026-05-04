
https://www.npmjs.com/package/node-onvif

http://front:80/cgi-bin/api.cgi?cmd=Snap&channel=0&rs=abc123&user=admin&password=******&width=640&height=480

https://github.com/agsh/onvif/blob/master/examples/example6.js

```
docker run -it --rm -v "$(pwd -W)":/app --name dev-sandbox -e NODE_ENV=docker -p 5000:5000 -p 8086:8086 node:22-alpine sh

docker exec -it dev-sandbox /bin/bash
```