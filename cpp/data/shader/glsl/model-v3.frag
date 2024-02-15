#version 330 core
/*
This is a shader program to render a model using simple Phong lighting.
*/

out vec4 out_color;

in vec2 frag_texcoord;
in vec3 frag_normal; //NEW
in vec3 frag_position; //NEW

uniform sampler2D u_textureBuffer;

uniform vec3 u_viewPosition;
uniform vec3 u_lightPosition = vec3(5,5,-5);
uniform vec3 u_lightColor = vec3(1.0f, 1.0f, 1.0f);

vec3 ambient(){
    float ambientStrength = 0.2f;
    return ambientStrength * u_lightColor;
}

vec3 diffuse(vec3 norm, vec3 lightDir){
    float diff = max(dot(norm, lightDir), 0.0);
    return (u_lightColor * diff);
}

vec3 specular(vec3 norm, vec3 lightDir){
    float specularStrength = 0.5;
    vec3 viewDir = normalize(u_viewPosition - frag_position);
    vec3 reflectDir = reflect(-lightDir, norm);

    float spec = pow(max(dot(viewDir, reflectDir), 0.0), 32);
    return specularStrength * spec * u_lightColor;
}

void main()
{
    vec3 norm = normalize(frag_normal);
    vec3 lightDir = normalize(u_lightPosition - frag_position);

    vec4 texColor = texture(u_textureBuffer, frag_texcoord);
    vec4 lightColor = vec4((ambient() + diffuse(norm, lightDir) + specular(norm, lightDir)), 1.0f);
    out_color = texColor * lightColor;
} 