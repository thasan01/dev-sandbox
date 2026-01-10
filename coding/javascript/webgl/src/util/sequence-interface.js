exports.SequenceInterface = class {
  constructor({ initial = 0, max = Number.MAX_VALUE }) {
    this.current = initial;
    this.max = max;
  }

  next() {
    throw new Error("Function not implemented.");
  }

  peek() {
    throw new Error("Function not implemented.");
  }

  release(id) {
    throw new Error("Function not implemented.");
  }
};
