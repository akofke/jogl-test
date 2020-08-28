#version 330 core

layout (points) in;
layout (triangle_strip, max_vertices = 4) out;

uniform mat4 projection;

flat out vec3 sphere_center;
flat out float radius_sq;
smooth out vec3 frag_pos;

void emitVertex(float radius, float rsq, vec3 displacement) {
    radius_sq = rsq;
    sphere_center = gl_in[0].gl_Position.xyz;
    frag_pos = gl_in[0].gl_Position.xyz + displacement * radius;
    gl_Position = projection * (gl_in[0].gl_Position + vec4(displacement * radius, 0));
    EmitVertex();
}

void emitVertex2(in vec2 displacement) {
    float radius = 0.1;
    const float overdraw = 1.1;
    displacement *= overdraw;
    radius_sq = radius * radius;
    sphere_center = gl_in[0].gl_Position.xyz;
    vec3 position = gl_in[0].gl_Position.xyz + vec3(radius * displacement, radius);
    frag_pos = position;
    gl_Position = projection * vec4(position, gl_in[0].gl_Position.w);
    EmitVertex();
}

void main() {

    float radius = 1;
    float rsq = radius * radius;

    emitVertex2(vec2(-1.0, -1.0));
    emitVertex2(vec2(-1.0, +1.0));
    emitVertex2(vec2(+1.0, -1.0));
    emitVertex2(vec2(+1.0, +1.0));
    EndPrimitive();

//    emitVertex(radius, rsq, vec3(1, 1, 1));
//
//    emitVertex(radius, rsq, vec3(1, -1, 1));
//
//    emitVertex(radius, rsq, vec3(1, 1, -1));
//
//    emitVertex(radius, rsq, vec3(1, -1, -1));
//
//    emitVertex(radius, rsq, vec3(-1, -1, -1));
//
//    emitVertex(radius, rsq, vec3(1, -1, 1));
//
//    emitVertex(radius, rsq, vec3(-1, -1, 1));
//
//    emitVertex(radius, rsq, vec3(1, 1, 1));
//
//    emitVertex(radius, rsq, vec3(-1, 1, 1));
//
//    emitVertex(radius, rsq, vec3(1, 1, -1));
//
//    emitVertex(radius, rsq, vec3(-1, 1, -1));
//
//    emitVertex(radius, rsq, vec3(-1, -1, -1));
//
//    emitVertex(radius, rsq, vec3(-1, 1, 1));
//
//    emitVertex(radius, rsq, vec3(-1, -1, 1));
//
//    EndPrimitive();

}
