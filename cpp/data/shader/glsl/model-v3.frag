#version 330 core
/*
This is a shader program to render a model using simple Phong lighting.
*/

struct Material {
    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
    float shininess;
}; 

struct Light {
    vec3 position;  
    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
};  

out vec4 out_color;

in vec2 frag_texcoord;
in vec3 frag_normal; //NEW
in vec3 frag_position; //NEW

uniform sampler2D u_textureBuffer;

uniform Material material;
uniform Light light; 

uniform vec3 u_viewPosition;

vec3 ambient(){
    return light.ambient * material.ambient;
}

vec3 diffuse(vec3 norm, vec3 lightDir){
    float diff = max(dot(norm, lightDir), 0.0);
    return light.diffuse * (diff * material.diffuse);    
}

vec3 specular(vec3 norm, vec3 lightDir){
    vec3 viewDir = normalize(u_viewPosition - frag_position);
    vec3 reflectDir = reflect(-lightDir, norm);

    float spec = pow(max(dot(viewDir, reflectDir), 0.0), material.shininess);
    return light.specular * (spec * material.specular);
}

void main()
{
    vec3 norm = normalize(frag_normal);
    vec3 lightDir = normalize(light.position - frag_position);

    vec4 texColor = texture(u_textureBuffer, frag_texcoord);
    vec4 lightColor = vec4((ambient() + diffuse(norm, lightDir) + specular(norm, lightDir)), 1.0f);
    out_color = texColor * lightColor;
} 