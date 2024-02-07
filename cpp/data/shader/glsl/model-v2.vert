#version 330 core
/*
This is a shader program to render a model using a simple camera.
*/

layout (location = 0) in vec3 in_vertex;
layout (location = 1) in vec3 in_normal;
layout (location = 2) in vec2 in_texcoord;

out vec2 frag_texcoord;

//NEW: (Project x View x Model) matrix
uniform mat4 u_pvmMatrix; 

void main()
{
    frag_texcoord = in_texcoord;
    //NEW: transform the vertex based on the pvm matrix
    gl_Position = u_pvmMatrix * vec4(in_vertex, 1.0);
}