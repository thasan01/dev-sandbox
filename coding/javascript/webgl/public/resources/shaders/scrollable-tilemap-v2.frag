#version 300 es
precision mediump float;

in vec2 v_texCoord; 
flat in int tileId; 
flat in vec2 v_uvOffset; //NEW: calculated in vertex shader
flat in vec2 v_texScalar; //NEW: calculated in vertex shader

uniform sampler2D u_image; 

out vec4 pixelColor;

void main(void) {    
    pixelColor = texture(u_image, ((v_texCoord + v_uvOffset) * v_texScalar).xy); 
}