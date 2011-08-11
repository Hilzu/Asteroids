#version 330

out vec4 vFragColor; // Fragment color to rasterize
in vec4 vVaryingColor; // Incoming color from vertex stage

void main(void)
    {
    vFragColor = vVaryingColor;
    }
