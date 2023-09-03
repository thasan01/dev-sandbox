exports.renderer = ({ canvas, shaderPrograms }) => {
  const { shaderParser } = require("./shader-parser");
  const { shaderProgram } = require("./shader-program");
  const { texture } = require("./texture");

  function createShader(ctx, type, source) {
    var shader = ctx.createShader(type);
    ctx.shaderSource(shader, source);
    ctx.compileShader(shader);
    var success = ctx.getShaderParameter(shader, ctx.COMPILE_STATUS);

    if (success) {
      return shader;
    }

    console.log(ctx.getShaderInfoLog(shader));
    ctx.deleteShader(shader);
  }

  function createProgram(vertexShaders, fragmentShaders) {
    var program = gl.createProgram();

    for (let shader of vertexShaders) gl.attachShader(program, shader);
    for (let shader of fragmentShaders) gl.attachShader(program, shader);

    gl.linkProgram(program);
    var success = gl.getProgramParameter(program, gl.LINK_STATUS);

    if (success) {
      return program;
    }

    console.log(gl.getProgramInfoLog(program));
    gl.deleteProgram(program);
  }

  function resizeCanvasToDisplaySize(renderCanvas) {
    // Lookup the size the browser is displaying the canvas in CSS pixels.
    const displayWidth = renderCanvas.clientWidth;
    const displayHeight = renderCanvas.clientHeight;

    // Check if the canvas is not the same size.
    const needResize =
      renderCanvas.width !== displayWidth ||
      renderCanvas.height !== displayHeight;

    if (needResize) {
      // Make the canvas the same size
      renderCanvas.width = displayWidth;
      renderCanvas.height = displayHeight;
    }

    return needResize;
  }

  function render(func) {
    resizeCanvasToDisplaySize(gl.canvas);
    gl.viewport(0, 0, gl.canvas.width, gl.canvas.height);

    func({ context: gl });
  }

  function loadImage(filename, callback) {
    const image = new Image();
    image.setAttribute("crossOrigin", "anonymous");
    image.src = filename;

    return new Promise((resolve) => {
      image.onload = (data) => {
        if (callback) callback(image);

        resolve(image);
      };
    });
  }

  function loadTexture(image) {
    let textureId = gl.createTexture();

    gl.bindTexture(gl.TEXTURE_2D, textureId);
    //*
    gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_S, gl.MIRRORED_REPEAT);
    gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_T, gl.MIRRORED_REPEAT);
    gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MAG_FILTER, gl.NEAREST);
    gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MIN_FILTER, gl.NEAREST);
    //*/
    gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_S, gl.CLAMP_TO_EDGE);
    gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_T, gl.CLAMP_TO_EDGE);

    gl.texImage2D(gl.TEXTURE_2D, 0, gl.RGBA, gl.RGBA, gl.UNSIGNED_BYTE, image);
    gl.bindTexture(gl.TEXTURE_2D, null);

    return texture(gl, textureId, 0);
  }

  function createArrayBuffer(
    data,
    bufferObject = null,
    usage = gl.STATIC_DRAW
  ) {
    if (!bufferObject) bufferObject = gl.createBuffer();

    gl.bindBuffer(gl.ARRAY_BUFFER, bufferObject);
    gl.bufferData(gl.ARRAY_BUFFER, data, usage);
    return bufferObject;
  }

  function createUniformBuffer({
    bufferObject = null,
    srcData = null,
    usage = gl.STATIC_DRAW,
    srcOffset = 0,
    length = 0,
  }) {
    if (!bufferObject) bufferObject = gl.createBuffer();

    let target = gl.UNIFORM_BUFFER;
    gl.bindBuffer(target, bufferObject);

    if (srcData) {
      gl.bufferData(target, srcData, usage, srcOffset, length);
    }
    return bufferObject;
  }

  function createElementBuffer(data, usage = gl.STATIC_DRAW) {
    var bufferObject = gl.createBuffer();
    gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, bufferObject);
    gl.bufferData(gl.ELEMENT_ARRAY_BUFFER, data, usage);
  }

  //======================
  // Entry Point
  //======================

  let gl = canvas.getContext("webgl2");

  if (!gl) {
    throw new Error("WebGL not supported.");
  }

  let outShaderPrograms = {};

  for (let program of shaderPrograms) {
    let vertShaders = [];
    let fragShaders = [];
    let metadata = {};

    if (program.vertexShaders) {
      for (let shaderSource of program.vertexShaders) {
        vertShaders.push(createShader(gl, gl.VERTEX_SHADER, shaderSource));
        Object.assign(metadata, shaderParser(shaderSource));
      }
    }

    if (program.franmentShaders) {
      for (let shaderSource of program.franmentShaders) {
        fragShaders.push(createShader(gl, gl.FRAGMENT_SHADER, shaderSource));
        Object.assign(metadata, shaderParser(shaderSource));
      }
    }

    var programId = createProgram(vertShaders, fragShaders);

    //update the glsl location of the metadata variables from the program
    for (const varName in metadata) {
      let entry = metadata[varName];
      if (entry.qualifier === "in")
        entry.location = gl.getAttribLocation(programId, varName);
      else if (entry.qualifier === "uniform")
        entry.location = gl.getUniformLocation(programId, varName);
    }

    outShaderPrograms[program.name] = shaderProgram(
      gl,
      program.name,
      programId,
      metadata
    );
  }

  return {
    render,
    loadImage,
    loadTexture,
    createArrayBuffer,
    createElementBuffer,
    createUniformBuffer,
    context: gl,
    shaderPrograms: outShaderPrograms,
  };
};
