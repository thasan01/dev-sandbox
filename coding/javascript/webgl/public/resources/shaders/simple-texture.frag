#version 300 es
precision mediump float;
out vec4 pixelColor;

uniform sampler2D u_image; //NEW: Define the texture data
in vec2 v_texCoord; //NEW: contains interpolated textcoord value passed from vertex shader

void main(void) {    
    pixelColor = texture(u_image, v_texCoord); //NEW: calculate color from texture
}