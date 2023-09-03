exports.SystemDetails = (() => {
  function isBigEndian() {
    let uint32 = new Uint32Array([0x12000000]);
    let uint8 = new Uint8Array(uint32.buffer);
    return uint8[0] === 0x12;
  }

  return {
    isLittleEndian: !isBigEndian(),
  };
})();
