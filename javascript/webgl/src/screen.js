exports.Screen = ({ root, width = 640, height = 480 }) => {
  let counter = 0;
  const prefix = "screen_layer_";

  root.style.width = `${width}px`;
  root.style.height = `${height}px`;

  function addLayer({ id, cssOpts }) {
    let newId = id ? id : [prefix, counter++].join("");
    var canvas = document.createElement("canvas");
    canvas.id = newId;
    canvas.width = width;
    canvas.height = height;

    for (let key in cssOpts) canvas.style[key] = cssOpts[key];
    canvas.style.zIndex = root.children.length + 1;
    root.appendChild(canvas);
    return newId;
  }

  function removeLayer(id) {
    let child = [...root.childNodes].find((elem) => elem.id === id);
    if (child) {
      root.removeChild(child);
    }
  }

  return { addLayer, removeLayer };
};
