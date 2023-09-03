#version 300 es
in vec3 a_position;
in vec2 a_texCoord; //NEW: define input texture coord attribute
out vec2 v_texCoord; //NEW: define output varring texture coord. This varible will have default 'smooth' qualifier. Therefore, the value will be interpolated.

uniform mat4 u_mvpMatrix; // (projection * view * model)
    
//This Shader pass texture coord from Vertex into Fragment shader
void main()
{
    v_texCoord = a_texCoord; //NEW: pass the value.
    gl_Position = u_mvpMatrix * vec4(a_position, 1.0);
}    