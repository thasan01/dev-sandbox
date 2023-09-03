#version 300 es
precision mediump float;

in vec2 v_texCoord; 
flat in int tileId; 

uniform sampler2D u_image; 
uniform ivec2 u_image_dim; //contains number of tiles in each (row,coulmn) of tilemap
uniform int u_tile_map[500];

uniform mediump ivec2 u_start_coord; //NEW:
uniform mediump  ivec2 u_screen_dim; //NEW: contains number of columns/rows in the screen
uniform mediump ivec2 u_tilemap_dim; //NEW: contains number of columns/rows in the tilemap

out vec4 pixelColor;

int func(int tileId) {
    int tcol = u_tilemap_dim[0];
    int scol = u_screen_dim[0];

    int offset = tcol - scol;
    int start_idx = (u_start_coord.y * tcol) + u_start_coord.x;
    int local_y = tileId / scol;
    int local_x = tileId % scol;
    return (local_y * scol) + local_x + (local_y * offset) + start_idx;
}

//convert tileId into (u,v) texture coordinate
vec2 tileIdToCoord(int index) {
    float y = floor(float(index) / float(u_image_dim.x));
    float x = float(index % u_image_dim.x);
    return vec2(x, y);
}

void main(void) {    
    vec2 tile_scalar = vec2(1.0 / float(u_image_dim.x), 1.0 / float(u_image_dim.y) );
    vec2 uv_offset = tileIdToCoord(u_tile_map[ func(tileId) ]);
    pixelColor = texture(u_image, ((v_texCoord + uv_offset) * tile_scalar).xy); 
}