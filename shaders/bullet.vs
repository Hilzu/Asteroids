#version 140

in vec4 vVertex;    // Vertex position attribute
in vec4 vColor;     // Vertex color attribute

uniform mat4 mvpMat;    // Modelview Projection matrix

smooth out vec4 vFragColor;     // Color value passed to fragment shader

void main(void)
{
    gl_PointSize = 8.0;
    vFragColor = vColor;     // Pass the color to fragment shader
    gl_Position = mvpMat * vVertex;     // Transform position with mvp
}
