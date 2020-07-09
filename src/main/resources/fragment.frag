#version 330 core
out vec4 FragColor;
in vec3 Normal;
in vec3 FragPos;

//uniform vec4 ourColor;

struct DirLight {
    vec3 direction;

    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
};


struct Material {
    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
    float shininess;
};

uniform vec3 viewPos;
uniform DirLight dirLight;
uniform Material material;

vec3 calcDirLight(DirLight light, vec3 normal, vec3 viewDir);

void main() {
    vec3 normal = normalize(Normal);
    vec3 viewDir = normalize(viewPos - FragPos);
    vec3 color = calcDirLight(dirLight, normal, viewDir);
    FragColor = vec4(color, 1.0);
}

vec3 calcDirLight(DirLight light, vec3 normal, vec3 viewDir) {
    vec3 lightDir = normalize(-light.direction);

    float diffuseFac = max(dot(normal, lightDir), 0.0);

    vec3 reflectDir = reflect(-lightDir, normal);
    float specularFac = pow(max(dot(viewDir, reflectDir), 0.0), material.shininess);

    vec3 ambient = light.ambient * material.diffuse;
    vec3 diffuse = light.diffuse * diffuseFac * material.diffuse;
    vec3 specular = light.specular * specularFac * material.specular;
    return (ambient + diffuse + specular);
}
