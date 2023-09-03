exports.MixedTypedArray = ({
  metadata,
  sourceBuffers = [],
  maxObjects = 100,
}) => {
  function copy(sourceBuffer, targetBuffer, metadata, schemaId) {
    let schema = metadata.fieldSchema[schemaId];
    let elementBytes = schema.type.BYTES_PER_ELEMENT;
    let srcStart = 0;
    let srcEnd = schema.elementCount;
    let numObjects = sourceBuffer.length / schema.elementCount;

    let trgStart = Math.ceil(schema.localOffset / elementBytes);
    let stride = metadata.objectBytes / elementBytes;

    for (let i = 0; i < numObjects; i++) {
      let subset = sourceBuffer.slice(srcStart, srcEnd);
      targetBuffer.set(subset, trgStart);

      trgStart += stride;
      srcStart += schema.elementCount;
      srcEnd += schema.elementCount;
    }
  }

  //============
  //Entry Point
  //============
  let localOffset = 0;
  let paddedByteSize = 0; //Contains the size of a single field with padding.This value is the max of the type.BYTES_PER_ELEMENT for all fieldSchemas.

  //Reusable temp varibales
  let bytesPerElement, schema, source;

  //Validate & calculate fieldSchame offset
  for (let i in metadata.fieldSchema) {
    schema = metadata.fieldSchema[i];
    source = sourceBuffers[i];
    bytesPerElement = schema.type.BYTES_PER_ELEMENT;

    if (source && source.length % schema.elementCount !== 0) {
      throw new Error(
        "Source[",
        i,
        "] length[",
        source.length,
        "] is not a multiple of elementCount defined in the schema[",
        schema.elementCount,
        "]."
      );
    }

    schema.localOffset = localOffset;
    localOffset += bytesPerElement * schema.elementCount;

    if (paddedByteSize < bytesPerElement) paddedByteSize = bytesPerElement;
  }

  metadata.objectBytes =
    Math.ceil(localOffset / paddedByteSize) * paddedByteSize;

  let numObjects = 0;
  if (sourceBuffers.length > 0) {
    numObjects = Math.floor(
      sourceBuffers[0].length / metadata.fieldSchema[0].elementCount
    );
  }

  metadata.maxObjects = maxObjects;
  metadata.objectCount = numObjects;
  metadata.arrayBytes = maxObjects * metadata.objectBytes;

  //Create the TypedArrays
  let buffer = new ArrayBuffer(metadata.arrayBytes);
  let targetBuffers = [];

  for (let i in metadata.fieldSchema) {
    schema = metadata.fieldSchema[i];
    source = sourceBuffers[i];

    //Initialize a new TypedArray view based on the field schema. Ex: Float32Array, etc.
    let bufferView = new schema.type(buffer);
    targetBuffers.push(bufferView);
    if (source) copy(source, bufferView, metadata, i);
  }

  return { metadata, targetBuffers };
};
