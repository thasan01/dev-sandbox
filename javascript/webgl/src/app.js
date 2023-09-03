const glMatrix = require("gl-matrix");
const { asynctask } = require("./asynctask");
const { renderer } = require("./renderer");
const { Screen } = require("./screen");
const { TileEngine } = require("./tile-engine");
const { MixedTypedArray } = require("./renderer/mixed-typed-array");
const { SpriteSystem } = require("./sprite-system");
const { SystemDetails } = require("./system-details");
const { RecyclableSequence } = require("./util/recyclable-sequence");
const Constants = require("./data/constants");
const jQuery = require("jquery");

const keyCode = { UP: 38, DOWN: 40, LEFT: 37, RIGHT: 39 };

exports.libs = { glMatrix, jQuery };
exports.modules = {
  Renderer: renderer,
  AsyncTask: asynctask,
  TileEngine,
  Screen,
  MixedTypedArray,
  SpriteSystem,
  RecyclableSequence,
  SystemDetails,
  Constants,
  keyCode,
};
exports.params = { serverUrl: "http:localhost:8080/resources" };
