#version 120

attribute vec3 vertices;
attribute vec2 textures;

varying vec2 textureCoords;

uniform mat4 projection;

void main() {
    textureCoords = textures;
    gl_Position = projection * vec4(vertices, 1);
}