#version 150

in vec4 vVertex;    // Vertex position attribute
in vec4 vColor;     // Vertex color attribute

smooth out vec4 vFragColor;     // Color value passed to fragment shader

void main() {
    vFragColor = vColor;    // Copy and pass color as-is
    gl_Position = vVertex;      // Set vertex position as is
}
