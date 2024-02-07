#version 330 core
/*
This is a shader program to render a model using a simple camera.
*/

out vec4 out_color;

in vec2 frag_texcoord;

uniform sampler2D u_textureBuffer;

void main()
{
    out_color = texture(u_textureBuffer, frag_texcoord);
} 