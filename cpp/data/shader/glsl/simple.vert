#version 330 core
#pragma optimize (off)
layout (location = 0) in vec3 in_vertices;
layout (location = 1) in vec3 in_normals;
layout (location = 2) in vec2 in_texcoords;

void main()
{
    gl_Position = vec4(in_vertices, 1.0);
}