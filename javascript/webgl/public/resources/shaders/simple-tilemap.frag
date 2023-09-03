#version 300 es
precision mediump float;

in vec2 v_texCoord; 
flat in int tileId; 

uniform sampler2D u_image; 
uniform ivec2 u_image_dim; //NEW: contains number of tiles in each (row,coulmn) of tilemap
uniform int u_tile_map[500];

out vec4 pixelColor;

//NEW: convert tileId into (u,v) texture coordinate
vec2 tileIdToCoord(int index) {
    float y = floor(float(index) / float(u_image_dim.x));
    float x = float(index % u_image_dim.x);
    return vec2(x, y);
}

void main(void) {    

    vec2 tile_scalar = vec2(1.0 / float(u_image_dim.x), 1.0 / float(u_image_dim.y) );
    vec2 uv_offset = tileIdToCoord(u_tile_map[tileId]);
    pixelColor = texture(u_image, ((v_texCoord + uv_offset) * tile_scalar).xy); 
}