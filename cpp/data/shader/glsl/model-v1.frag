#version 330 core
out vec4 out_color;

//NEW: interpolated texcoord value passed from the vertex shader
in vec2 frag_texcoord;

//NEW: stores the texture pixels as a buffer
uniform sampler2D u_textureBuffer;

void main()
{
    //NEW: calculate the final color based off the textcoord in the texture image 
    out_color = texture(u_textureBuffer, frag_texcoord);
} 