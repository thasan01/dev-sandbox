"use strict";
const { expect } = require("chai");
const reader = require("read-file");
const { shaderParser } = require("../../src/renderer/shader-parser");

describe("ShaderParser", function () {
  var shaderSource = reader.sync(
    "public/resources/shaders/simple-texture.vert",
    { encoding: "utf8" }
  );

  //Test1
  it("should parse shader", function () {
    const expected = {
      a_position: {
        qualifier: "in",
        dataType: "vec3",
        isArray: false,
        arraySize: null,
        location: null,
      },
      a_texCoord: {
        qualifier: "in",
        dataType: "vec2",
        isArray: false,
        arraySize: null,
        location: null,
      },
      v_texCoord: {
        qualifier: "out",
        dataType: "vec2",
        isArray: false,
        arraySize: null,
        location: null,
      },
      u_mvpMatrix: {
        arraySize: null,
        dataType: "mat4",
        isArray: false,
        location: null,
        qualifier: "uniform",
      },
    };

    let actual = shaderParser(shaderSource);
    expect(actual).to.eql(expected);
  });
});
