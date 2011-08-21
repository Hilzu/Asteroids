#version 140

smooth out vec4 vFragColor; // Fragment color to rasterize
smooth in vec4 vVaryingColor; // Incoming color from vertex stage

void main(void)
{
    vFragColor = vVaryingColor;
}
