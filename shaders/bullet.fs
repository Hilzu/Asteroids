#version 140

smooth in vec4 vFragColor;  // Color value passed by vertex/geometry shader
smooth out vec4 vColor;     // Color to draw on screen

void main() {
    if( dot(gl_PointCoord-0.5, gl_PointCoord-0.5) > 0.25 )
        discard;
    else
        vColor = vFragColor;
}