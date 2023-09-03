#version 300 es
in vec3 a_position;
in vec2 a_texCoord; 

flat out vec2 v_uvOffset; //NEW:
flat out vec2 v_texScalar; //NEW: 
out vec2 v_texCoord; 
flat out int tileId; 

//NEW: move matrix into shared uniform object  
layout (std140) uniform Camera{
    uniform mat4 mvp; // (projection * view * model)
} camera;

uniform mediump ivec2 u_screen_dim; //contains number of columns/rows in the screen
uniform mediump ivec2 u_tilemap_dim; //contains number of columns/rows in the tilemap
uniform mediump vec2 u_scroll_offset; //
uniform mediump ivec2 u_image_dim; //contains number of tiles in each (row,coulmn) of tilemap
uniform mediump ivec2 u_start_coord; //contains the start row/column of the tilemap
uniform int u_tile_map[500];

int tileMapIndex(int tileId) {
    int tcol = u_tilemap_dim[0]; //TileMap columns
    int scol = u_screen_dim[0]; //Screen columns

    int offset = tcol - scol;
    int start_idx = (u_start_coord.y * tcol) + u_start_coord.x;
    int local_y = tileId / scol;
    int local_x = tileId % scol;
    return (local_y * scol) + local_x + (local_y * offset) + start_idx;
}

//convert tileId into (u,v) texture coordinate
vec2 tileIdToCoord(int index, int numCols) {
    float y = floor(float(index) / float(numCols));
    float x = float(index % numCols);
    return vec2(x, y);
}

void main()
{
    v_texCoord = a_texCoord; 
    tileId = gl_InstanceID; 

    v_uvOffset = tileIdToCoord( u_tile_map[tileMapIndex(tileId)], u_image_dim.x );
    v_texScalar = vec2(1.0 / float(u_image_dim.x), 1.0 / float(u_image_dim.y) );

    vec3 offset = vec3( tileIdToCoord(tileId, u_screen_dim.x) + u_scroll_offset, 0); 
    gl_Position = camera.mvp * vec4(a_position + offset, 1.0); 
}    