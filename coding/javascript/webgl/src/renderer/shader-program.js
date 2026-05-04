const { RecyclableSequence } = require("../util/recyclable-sequence");

exports.shaderProgram = (gl, name, programId, metadata) => {
  let meta = metadata;

  let uboSeq = new RecyclableSequence();
  let uboBindingMap = {};

  function bindUbo(uboName, uboId) {
    bindingPoint = uboBindingMap[uboName];

    if (bindingPoint === null || bindingPoint === undefined) {
      bindingPoint = uboSeq.next();
      uboBindingMap[uboName] = bindingPoint;
    }

    let blockIndex = gl.getUniformBlockIndex(programId, uboName);
    gl.uniformBlockBinding(programId, blockIndex, bindingPoint);
    gl.bindBufferBase(gl.UNIFORM_BUFFER, blockIndex, uboId);
  }

  function use() {
    gl.useProgram(programId);
  }

  function unuse() {
    gl.useProgram(null);
  }

  function setAttribute(
    attrName,
    bufferObject,
    numComponents,
    dataTye,
    options
  ) {
    let {
      normalized = false,
      stride = 0,
      offset = 0,
      divisor = 0,
    } = options ? options : {};

    let loc = meta[attrName].location;
    gl.bindBuffer(gl.ARRAY_BUFFER, bufferObject);
    gl.enableVertexAttribArray(loc);
    gl.vertexAttribPointer(
      loc,
      numComponents,
      dataTye,
      normalized,
      stride,
      offset
    );
    gl.vertexAttribDivisor(loc, divisor);
  }

  function getVarLocation(varName) {
    let obj = metadata[varName];

    //Adding a fallback logic, incase the sharder parser fails.
    if (!obj) {
      const location = gl.getUniformLocation(programId, varName);
      if (location !== null) {
        let msg = `There was a problem parsing variable[{varName}] for ShaderProgram[{name}].`;
        console.warn(msg);
        obj = { location };
        metadata[varName] = obj;
      }
    }

    if (!obj || !obj.location)
      throw new Error("Unable to find location for GLSL variable " + varName);

    return obj.location;
  }

  function setVariable1uiv(varName, data) {
    let loc = getVarLocation(varName);
    gl.uniform1uiv(loc, data);
  }

  function setVariable1i(varName, i1) {
    let loc = getVarLocation(varName);
    gl.uniform1i(loc, i1);
  }

  function setVariable1iv(varName, data) {
    let loc = getVarLocation(varName);
    gl.uniform1iv(loc, data);
  }

  function setVariable2i(varName, i1, i2) {
    let loc = getVarLocation(varName);
    gl.uniform2i(loc, i1, i2);
  }

  function setVariable4i(varName, i1, i2, i3, i4) {
    let loc = getVarLocation(varName);
    gl.uniform4i(loc, i1, i2, i3, i4);
  }

  function setVariable2iv(varName, data) {
    let loc = getVarLocation(varName);
    gl.uniform2iv(loc, data);
  }

  function setVariable1fv(varName, data) {
    let loc = getVarLocation(varName);
    gl.uniform1fv(loc, data);
  }

  function setVariable2f(varName, f1, f2) {
    let loc = getVarLocation(varName);
    gl.uniform2f(loc, f1, f2);
  }

  function setVariable2fv(varName, value) {
    let loc = getVarLocation(varName);
    gl.uniform2fv(loc, value);
  }

  function setVariableMatrix4fv(varName, value, doTranspose = false) {
    let loc = getVarLocation(varName);
    gl.uniformMatrix4fv(loc, doTranspose, value);
  }

  return {
    context: gl,
    name,
    id: programId,
    use,
    unuse,
    bindUbo,
    setAttribute,
    setVariable1uiv,
    setVariable1i,
    setVariable2i,
    setVariable2iv,
    setVariable4i,
    setVariable1iv,
    setVariable1fv,
    setVariable2f,
    setVariable2fv,
    setVariableMatrix4fv,
  };
};
