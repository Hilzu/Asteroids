#version 150

smooth in vec4 vFragColor;  // Color value passed by vertex/geometry shader
smooth out vec4 vColor;     // Fragment color to rasterize

void main() {
    vColor = vFragColor;
}
