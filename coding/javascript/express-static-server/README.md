# express-static-server

This is a simple project to host static files using Express server.

| Environment Variables | Default | Description                                                                                                                                                                                       |
| --------------------- | ------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| EXPRESS_STATIC_LIST   | /public | A comma seperated list of file paths to expose via express server. For example `EXPRESS_STATIC_LIST=/public:C:\tmp\path1,/private:C:\tmp\path2` will create two url paths `public` and `private`. |
| EXPRESS_SERVER_PORT   | 8080    | The port to start the server.                                                                                                                                                                     |

To start the server, run the following command:

```bash
npm install & npm start
```
