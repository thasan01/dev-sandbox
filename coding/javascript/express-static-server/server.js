const { EXPRESS_STATIC_LIST = "public" } = process.env;
const { EXPRESS_SERVER_PORT = "8080" } = process.env;

var express = require("express");
var http = require("http");
var cors = require("cors");

var app = express();
var server = http.createServer(app);

app.use(cors());

for (let elem of EXPRESS_STATIC_LIST.split(",")) {
  const idx = elem.indexOf(":");
  if (idx > 0) {
    const urlPath = elem.substring(0, idx);
    const filePath = elem.substring(idx + 1);
    console.log(`Adding static urlPath=${urlPath}, filePath=${filePath}`);
    app.use(urlPath, express.static(filePath));
  } else {
    console.log("Adding static path=", elem);
    app.use(express.static(elem));
  }
}

console.log("listen to ", EXPRESS_SERVER_PORT);
server.listen(EXPRESS_SERVER_PORT);
