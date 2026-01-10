#version 300 es
in vec3 a_position;
in vec2 a_texCoord; 
in vec2 a_offset;

out vec2 v_texCoord; 
flat out int tileId; //NEW

uniform mat4 u_mvpMatrix; // (projection * view * model)

void main()
{
    v_texCoord = a_texCoord; 
    tileId = gl_InstanceID; //NEW: pass gl_InstanceID down to frangment shader.
    vec3 offset = vec3(a_offset, 0); //NEW
    gl_Position = u_mvpMatrix * vec4(a_position + offset, 1.0); //NEW
}    