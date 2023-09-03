const { MixedTypedArray } = require("./renderer/mixed-typed-array");

exports.SpriteSystem = ({
  gfxContext,
  shaderProgram,
  vertexBuffer,
  textureBuffer,
  renderer,
}) => {
  const { FLOAT, TRIANGLES, UNSIGNED_SHORT } = gfxContext;

  let texture = null;
  let syncBuffer = false; //determine whether to push buffer to gpu
  let nextCounter = 0;
  let spriteLookup = {}; //sprite id to buffer index lookup table

  let metadata = {
    fieldSchema: [
      {
        elementCount: 2, //Number of elements in the field value
        type: Float32Array, //The data type of each element
        localOffset: NaN, //Number of bytes from the start of the object to this field
      },
      {
        //spriteId
        elementCount: 1,
        type: Float32Array,
        localOffset: NaN,
      },
      {
        //tileId
        elementCount: 2,
        type: Float32Array,
        localOffset: NaN,
      },
    ],
    objectCount: NaN,
    objectBytes: NaN, // Total number of bytes for a single object (with padding) in the Mixed Buffer Array.
    arrayBytes: NaN, //Total number of bytes for the entire Mixed Buffer Array (with padding).
  };

  function spriteTexture(tex) {
    texture = tex;
    texture.bind();
    shaderProgram.use();
    shaderProgram.setVariable1i("u_image", texture.activeChannel);
  }

  function update() {
    if (syncBuffer) {
      //TODO: replace with "gl.bufferSubData(target, dstByteOffset, srcData, srcOffset, length)"
      renderer.createArrayBuffer(targetBuffers[0], offsetBuffer);
      syncBuffer = false;
    }
  }

  function render(numFaces) {
    shaderProgram.use();
    texture.bind();
    gfxContext.drawElementsInstanced(
      TRIANGLES,
      numFaces,
      UNSIGNED_SHORT,
      0,
      metadata.objectCount
    );
  }

  function updateSprite(obj, spriteIndex) {
    const { objectBytes, fieldSchema } = metadata;

    let objectOffset = spriteIndex * objectBytes;
    let idx = 0;
    let nextIdx;

    for (let i in fieldSchema) {
      let schema = fieldSchema[i];

      let trgIdx =
        (objectOffset + schema.localOffset) / schema.type.BYTES_PER_ELEMENT;

      nextIdx = idx + schema.elementCount;
      targetBuffers[i].set(obj.slice(idx, nextIdx), trgIdx);
      idx = nextIdx;
    }
  }

  function addSprite(obj) {
    const { objectCount: spriteIndex } = metadata;
    spriteLookup[nextCounter] = spriteIndex;

    updateSprite(obj, nextCounter);

    metadata.objectCount++;
    return nextCounter++;
  }

  /**
   * Creates a new sprite or update an existing sprite.
   *
   * @param {object} spriteObject -
   * @param {int} spriteId -
   */
  function set(spriteObject, spriteId) {
    //if no spriteId is provided, then create new sprite object
    if (spriteId === undefined || spriteId === null) {
      spriteId = addSprite(spriteObject);
    } else {
      let spriteIndex = spriteLookup[spriteId];
      updateSprite(spriteObject, spriteIndex);
    }

    syncBuffer = true;
    return spriteId;
  }

  /**
   * Removes the sprite so it is no longer rendered.
   *
   * @param {int} spriteId - The unique sprite Id returned when creating the sprite.
   */
  function remove(spriteId) {
    let spriteIndex = spriteLookup[spriteId];

    if (spriteIndex === undefined || spriteIndex > metadata.objectCount)
      throw new Error("Invalid error input " + spriteId);

    syncBuffer = true;
    delete spriteLookup[spriteId];
    metadata.objectCount--;

    let target = spriteIndex * metadata.objectBytes;
    let start = metadata.objectCount * metadata.objectBytes;
    let end = start + metadata.objectBytes;

    byteView.copyWithin(target, start, end);
  }

  function apply(spriteId, func) {
    let spriteIndex = spriteLookup[spriteId];
    let byteOffset = spriteIndex * metadata.objectBytes;
    func(dataView, byteOffset, metadata, targetBuffers);
    syncBuffer = true;
  }

  //=============
  // Entry Point
  //=============
  let mta = MixedTypedArray({ metadata });
  let { targetBuffers } = mta;

  let divisor = 1;
  let { objectBytes: stride } = metadata;
  let offsetBuffer = renderer.createArrayBuffer(targetBuffers[0]);
  let byteView = new Uint8Array(targetBuffers[0].buffer);
  let dataView = new DataView(targetBuffers[0].buffer);

  shaderProgram.use();
  shaderProgram.setAttribute("a_position", vertexBuffer, 3, FLOAT);
  shaderProgram.setAttribute("a_texCoord", textureBuffer, 2, FLOAT, {
    normalized: true,
  });

  shaderProgram.setAttribute(
    "a_spriteOffset",
    offsetBuffer,
    metadata.fieldSchema[0].elementCount,
    FLOAT,
    {
      stride,
      divisor,
      offset: metadata.fieldSchema[0].localOffset,
    }
  );

  shaderProgram.setAttribute(
    "a_spriteId",
    offsetBuffer,
    metadata.fieldSchema[1].elementCount,
    FLOAT,
    {
      stride,
      divisor,
      offset: metadata.fieldSchema[1].localOffset,
    }
  );

  shaderProgram.setAttribute(
    "a_screenTileOffset",
    offsetBuffer,
    metadata.fieldSchema[2].elementCount,
    FLOAT,
    {
      stride,
      divisor,
      offset: metadata.fieldSchema[2].localOffset,
    }
  );

  return { apply, set, remove, spriteTexture, update, render };
};
