#version 140

in vec4 vVertex; // Vertex position attribute
in vec4 vColor; // Vertex color attribute

uniform mat4 mvpMat;    // Modelview Projection matrix

smooth out vec4 vVaryingColor; // Color value passed to fragment shader

void main(void)
{
    vVaryingColor = vColor;// Simply copy the color value
    gl_Position = mvpMat * vVertex;
}
