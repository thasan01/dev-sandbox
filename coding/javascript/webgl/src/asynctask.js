exports.asynctask = (args) => {
  let lastTime = 0;
  let requestID = 0;

  let defaultValues = {
    scheduleFunc: setInterval,
    cancelFunc: clearInterval,
    timer: 0,
    onStep: () => {},
  };

  let { scheduleFunc, cancelFunc, timer, onStep } = args || defaultValues;

  function loop(currentTime) {
    let dt = currentTime - lastTime;

    onStep(dt);

    lastTime = currentTime;
    requestID = scheduleFunc(loop, timer);
  }

  function start() {
    lastTime = 0;
    loop(0);
  }

  function stop() {
    cancelFunc(requestID);
    requestID = 0;
  }

  function toggle() {
    requestID > 0 ? stop() : start();
  }

  function callback(f) {
    onStep = f;
  }

  return { start, stop, toggle, callback };
};
