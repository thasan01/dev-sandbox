module.exports = (() => {
  let vert_coords = new Float32Array([
    -0.5, -0.5, 0,

    0.5, -0.5, 0,

    0.5, 0.5, 0,

    -0.5, 0.5, 0,
  ]);

  //for (let i = 0; i < vert_coords.length; i = i + 3) {
  //  vert_coords[i] += 0.5;
  //  vert_coords[i + 1] += 0.5;
  //}

  let text_coords = new Float32Array([
    0, 0,

    1, 0,

    1, 1,

    0, 1,
  ]);

  let face_indices = new Uint16Array([0, 2, 1, 0, 3, 2]);

  return { vert_coords, text_coords, face_indices };
})();
