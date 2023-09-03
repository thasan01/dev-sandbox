#version 300 es

precision highp float;

in vec3 a_position;
in vec2 a_texCoord; //define input texture coord attribute
in vec2 a_spriteOffset; 
in float a_spriteId; 
in vec2 a_screenTileOffset; //index of u_screen_pos

flat out vec2 v_uvOffset; //
flat out vec2 v_texScalar; // 
out vec2 v_texCoord; // define output varring texture coord. This varible will have default 'smooth' qualifier. Therefore, the value will be interpolated.

uniform ivec2 u_image_dim;
uniform vec2 u_start_coord; //NEW: list of x/y positions of screen tiles  

//NEW: move matrix into shared uniform object  
layout (std140) uniform Camera{
    uniform mat4 mvp; // (projection * view * model)
} camera;

//NEW:
layout (std140) uniform ScrollDetails{
    uniform vec2 startCoord; 
    uniform vec2 screenOffset;
} scroll;


vec2 getUVOffset(uint tileId, uint numCols){
    uint y = tileId / numCols;
    uint x = tileId % numCols;
    return vec2(float(x),float(y));
}

void main()
{
    v_texScalar = vec2(1.0 / float(u_image_dim.x), 1.0 / float(u_image_dim.y) );
    v_texCoord = a_texCoord; //pass the value.
    v_uvOffset = getUVOffset(uint(a_spriteId), uint(u_image_dim.x));

    vec2 tilepos = a_spriteOffset + (a_screenTileOffset - scroll.startCoord);
    vec3 pos = a_position + vec3(tilepos, 0); //TODO: update with tile pos
    gl_Position = camera.mvp * vec4(pos, 1.0);
}