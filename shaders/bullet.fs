#version 150

smooth in vec4 vFragColor;  // Color value passed by vertex/geometry shader
smooth out vec4 vColor;     // Color to raster

void main() {
    if( dot(gl_PointCoord-0.5, gl_PointCoord-0.5) > 0.25 ) {  // Make default square dots round
        discard;
    }
    else {
        vColor = vFragColor;
    }
}