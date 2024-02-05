#version 330 core
#pragma optimize (off)
layout (location = 0) in vec3 in_vertex;
layout (location = 1) in vec3 in_normal;
layout (location = 2) in vec2 in_texcoord;

void main()
{
    gl_Position = vec4(in_vertex, 1.0);
}