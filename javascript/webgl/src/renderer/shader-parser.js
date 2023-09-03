exports.shaderParser = (source) => {
  var invariantPattern = /(invariant\s+)?/;
  var inputPattern = /(in\s+|out\s+|uniform\s+)/;
  var dataypePattern =
    /([u]?int\s+|float\s+|[b|i]?vec\d+\s+|[b|i]?mat\d+\s+|sampler2D\s+)?/;
  var precisionPattern = /(lowp\s+|mediump\s+|highp\s+)?/;
  var variablePattern = /([a-zA-Z0-9_]+(\[\d+\])?)/;
  let arrayPattern = /([a-zA-Z0-9_]+)\[(\d+)\]/;
  let commentPattern = /\s*;\s*(\/\/)\s*/;

  var regex = new RegExp(
    "^s*" +
      invariantPattern.source +
      inputPattern.source +
      precisionPattern.source +
      dataypePattern.source +
      variablePattern.source +
      ".*"
  );

  function processLine(line, meta) {
    let res = regex.exec(line);
    if (res != null) {
      res = res.map((elem) => {
        return typeof elem === "string" ? elem.trim() : elem;
      });

      let [, invariant, qualifier, precision, dataype, variableName, array] =
        res;

      let isArray = array !== null;
      let arraySize, arrayRes;

      if (isArray && (arrayRes = arrayPattern.exec(variableName))) {
        [, variableName, arraySize] = arrayRes;
      }

      meta[variableName] = {
        invariant,
        qualifier,
        dataype,
        precision,
        variableName,
        isArray,
        arraySize,
        location: null,
      };
    }
  }

  if (!source) throw new Error("Invalid input[source].");

  let meta = {};
  let lines = source.split("\n");

  for (let i in lines) {
    let line = lines[i];

    let idx = line.search(commentPattern);
    if (idx > 0) {
      line = line.substr(0, idx);
    }

    processLine(line, meta);
  }

  return meta;
};
