const { SequenceInterface } = require("./sequence-interface");

exports.RecyclableSequence = class extends SequenceInterface {
  constructor(args) {
    super(args ? args : {});
    this.released = [];
  }

  next() {
    return this.released.length > 0 ? this.released.pop() : this.current++;
  }

  peek() {
    return this.released.length > 0
      ? this.released[this.released.length - 1]
      : this.current;
  }

  release(id) {
    this.released.push(id);
  }
};
