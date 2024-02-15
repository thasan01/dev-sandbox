#version 330 core
/*
This is a shader program to render a model using simple Phong lighting.
*/

layout (location = 0) in vec3 in_vertex;
layout (location = 1) in vec3 in_normal;
layout (location = 2) in vec2 in_texcoord;

out vec2 frag_texcoord;
out vec3 frag_normal; //NEW:
out vec3 frag_position; //NEW: the fragment position of the vertex 

uniform mat4 u_model; 
uniform mat4 u_view; 
uniform mat4 u_projection; 

void main()
{
    vec4 vert4 = vec4(in_vertex, 1.0);

    frag_texcoord = in_texcoord;
    frag_normal =  mat3(transpose(inverse(u_model))) * in_normal; //NEW:
    frag_position = vec3(u_model * vert4); //NEW:

    //NEW: calculate (Project x View x Model) matrix
    mat4 pvm = u_projection * u_view * u_model;

    //transform the vertex based on the pvm matrix
    gl_Position = pvm * vert4;
}