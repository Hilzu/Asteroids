#version 150

in vec4 vVertex;    // Vertex position attribute
in vec4 vColor;     // Vertex color attribute

uniform mat4 mvpMat;    // Modelview Projection matrix

smooth out vec4 vFragColor;     // Color value passed to fragment shader

void main() {
    vFragColor = vColor;     // Copy and pass color as-is
    gl_Position = mvpMat * vVertex;     // Transform vertex using mvp
}
