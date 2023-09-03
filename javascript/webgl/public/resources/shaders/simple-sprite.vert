#version 300 es

precision highp float;

in vec3 a_position;
in vec2 a_texCoord; //NEW: define input texture coord attribute
in vec2 a_spriteOffset; 
in float a_spriteId; 


flat out vec2 v_uvOffset; //NEW:
flat out vec2 v_texScalar; //NEW: 
out vec2 v_texCoord; //NEW: define output varring texture coord. This varible will have default 'smooth' qualifier. Therefore, the value will be interpolated.

uniform ivec2 u_image_dim;
uniform mat4 u_mvpMatrix; // (projection * view * model)

vec2 getUVOffset(uint tileId, uint numCols){
    uint y = tileId / numCols;
    uint x = tileId % numCols;
    return vec2(float(x),float(y));
}

//This Shader pass texture coord from Vertex into Fragment shader
void main()
{
    v_texScalar = vec2(1.0 / float(u_image_dim.x), 1.0 / float(u_image_dim.y) );
    v_texCoord = a_texCoord; //NEW: pass the value.
    v_uvOffset = getUVOffset(uint(a_spriteId), uint(u_image_dim.x));

    vec3 pos = a_position + vec3(a_spriteOffset, 0);
    gl_Position = u_mvpMatrix * vec4(pos, 1.0);
}    