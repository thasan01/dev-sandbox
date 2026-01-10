#version 300 es
in vec3 a_position;
in vec2 a_texCoord; 

out vec2 v_texCoord; 
flat out int tileId; 

uniform mat4 u_mvpMatrix; // (projection * view * model)

uniform mediump ivec2 u_screen_dim; //NEW: contains number of columns/rows in the screen
uniform mediump ivec2 u_tilemap_dim; //NEW: contains number of columns/rows in the tilemap
uniform mediump vec2 u_scroll_offset; //NEW:

vec3 calculateTileOffset(int tileId){
    int numScreenCols = u_screen_dim.x;

    float x = float(tileId % numScreenCols);
    float y = floor(float(tileId) / float(numScreenCols));

    return vec3(x, y, 0);
}

void main()
{
    v_texCoord = a_texCoord; 
    tileId = gl_InstanceID; 
    vec3 offset = calculateTileOffset(tileId) + vec3(u_scroll_offset,0); 
    gl_Position = u_mvpMatrix * vec4(a_position + offset, 1.0); 
}    