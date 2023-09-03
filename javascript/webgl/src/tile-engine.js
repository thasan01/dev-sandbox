exports.TileEngine = (args) => {
  //
  const EVENT_TYPES = {
    SCROLL_LEFT: 0,
    SCROLL_RIGHT: 1,
    SCROLL_UP: 2,
    SCROLL_DOWN: 3,
  };
  //
  let eventListner = () => {};

  const { numTileCols, numTileRows } = args;

  const lastColIndex = Math.max(0, numTileCols - 1);
  const lastRowIndex = Math.max(0, numTileRows - 1);
  const columnRatio = SCREEN_WIDTH / TILE_WIDTH;
  const rowRatio = SCREEN_HEIGHT / TILE_HEIGHT;

  //How many columns/rows of tiles can fit in the screen
  const numScreenCols = Math.ceil(columnRatio);
  const numScreenRows = Math.ceil(rowRatio);

  let { origin: focus = [0, 0], numBorderTiles = 1, tileSize = 1 } = args;
  let scrollOffset = new Float32Array([0, 0]);

  /**
   * Sets up the tiles in the screen. If the screen can hold entire tilemap, then the tiles are rendered in the center of the screen.
   * If there are more tiles in the map, than the screen can display, then this method calculates the subset of tiles to render in the screen.
   *
   * @param {number} screenUnit - Number of columns/rows in screen.
   * @param {number} numTileMapUnit - Number of columns/rows in the tilemap.
   * @param {number} tileUnit - Size of length/width of single tile.
   * @param {number} focusIndex - The x/y coord of the origin.
   * @param {number} numBorderTiles - Extra number of tiles to add to each edge of the screen. Used for padding.
   *
   **/
  function processDimension(
    screenUnit,
    numTileMapUnit,
    tileUnit,
    focusIndex,
    numBorderTiles = 1
  ) {
    let start, end;
    let tileOffset = -numBorderTiles;
    let maxScreenTiles; //total number of tiles, with the padding
    let halfScreen = Math.floor(screenUnit / 2);
    let remainder = screenUnit % 2 == 0 ? 1 : 0;
    let noScrolling = numTileMapUnit <= screenUnit;

    //Can the screen hold the entire tilemap?
    if (noScrolling) {
      maxScreenTiles = numTileMapUnit;
      start = 0;
      end = maxScreenTiles - 1;

      let halfMap = Math.floor(numTileMapUnit / 2);
      tileOffset = (halfScreen - halfMap) * tileUnit;
    } else {
      start = focusIndex - halfScreen - numBorderTiles + remainder;
      end = focusIndex + halfScreen + numBorderTiles;

      let lastMapIndex = numTileMapUnit - 1;
      let leftOverflow = 0;
      let rightOverflow = 0;

      if (start < 0) {
        leftOverflow = Math.abs(start);
        start = 0;
        end += leftOverflow;
        tileOffset = 0;
      }

      if (end > lastMapIndex) {
        rightOverflow = end - lastMapIndex;
        end = lastMapIndex;
        start = Math.max(0, start - rightOverflow);
        tileOffset = -Math.max(0, end - start + 1 - screenUnit);
      }

      maxScreenTiles = end - start + 1;
    }

    return {
      start,
      end,
      tileOffset,
      noScrolling,
      maxScreenTiles,
    };
  }

  function register(callback) {
    eventListner = callback;
  }

  function scroll(movex, movey, shaderProgram) {
    const movingLeft = movex > 0;
    const movingRight = movex < 0;
    const movingUp = movey > 0;
    const movingDown = movey < 0;
    let colThreshold = -2 * tileSize;
    let rowThreshold = -2 * tileSize;
    let [originX, originY] = scrollOffset;

    if (!columnData.noScrolling && (movingLeft || movingRight)) {
      scrollOffset[0] += movex;

      let isfirst = columnData.start <= 0;
      let islast = columnData.end >= lastColIndex;

      if (isfirst && scrollOffset[0] > 0) {
        scrollOffset[0] = 0;
      }

      if (islast && scrollOffset[0] < colThreshold) {
        scrollOffset[0] = colThreshold;
      }

      //Scroll left
      if (!isfirst && movingLeft && scrollOffset[0] > -tileSize) {
        scrollOffset[0] -= tileSize;

        let add = --columnData.start;
        let remove = columnData.end--;
        startCoord[0] = columnData.start;

        eventListner({
          event: EVENT_TYPES.SCROLL_LEFT,
          add,
          remove,
          localOrigin: startCoord,
        });
      } else if (!islast && movingRight && scrollOffset[0] < -tileSize) {
        scrollOffset[0] += tileSize;

        let remove = ++columnData.start;
        let add = ++columnData.end;
        startCoord[0] = columnData.start;

        eventListner({
          event: EVENT_TYPES.SCROLL_RIGHT,
          add,
          remove,
          localOrigin: startCoord,
        });
      }
    }

    if (!rowData.noScrolling && (movingUp || movingDown)) {
      let isfirst = rowData.start <= 0;
      let islast = rowData.end >= lastRowIndex;

      scrollOffset[1] += movey;

      if (isfirst && scrollOffset[1] > 0) {
        scrollOffset[1] = 0;
      }

      if (islast && scrollOffset[1] < rowThreshold) {
        scrollOffset[1] = rowThreshold;
      }

      //Scroll top
      if (!isfirst && movingUp && scrollOffset[1] > -tileSize) {
        scrollOffset[1] -= tileSize;

        let add = rowData.start--;
        let remove = --rowData.end;
        startCoord[1] = rowData.start;

        eventListner({
          event: EVENT_TYPES.SCROLL_UP,
          add,
          remove,
          localOrigin: startCoord,
        });
      }
      //Scroll bottom
      else if (!islast && movingDown && scrollOffset[1] < -tileSize) {
        scrollOffset[1] += tileSize;

        let remove = ++rowData.start;
        let add = rowData.end++;
        startCoord[1] = rowData.start;

        eventListner({
          event: EVENT_TYPES.SCROLL_UP,
          add,
          remove,
          localOrigin: startCoord,
        });
      }
    }

    shaderProgram.setVariable2fv("u_scroll_offset", scrollOffset);
    shaderProgram.setVariable2iv("u_start_coord", startCoord);

    return [
      Math.abs(originX - scrollOffset[0]) > 0,
      Math.abs(originY - scrollOffset[1]) > 0,
    ];
  }

  //=====================
  // Initialization logic
  //=====================
  let columnData = processDimension(
    numScreenCols,
    numTileCols,
    1,
    focus[0],
    numBorderTiles
  );
  let rowData = processDimension(
    numScreenRows,
    numTileRows,
    1,
    focus[1],
    numBorderTiles
  );

  startCoord = [columnData.start, rowData.start];
  scrollOffset = [columnData.tileOffset, rowData.tileOffset];

  return {
    scroll,
    register,
    EVENT_TYPES,
    schema: {
      column: columnData,
      row: rowData,
    },
  };
};
