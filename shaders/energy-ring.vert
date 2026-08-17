attribute vec4 a_position;
attribute vec3 a_normal;
attribute vec2 a_texCoord0;
attribute float a_surface;

uniform mat4 u_proj;
uniform mat4 u_trans;
uniform highp float u_time;

varying vec2 v_uv;
varying vec3 v_worldPos;
varying vec3 v_worldNormal;
varying float v_surface;

void main(){
    vec3 ringNormal = vec3(0.0, 0.8660254, 0.5);
    vec3 ringForward = vec3(0.0, -0.5, 0.8660254);
    vec3 radial = normalize(a_position.xyz - ringNormal * dot(a_position.xyz, ringNormal));
    float angle = atan(dot(radial, ringForward), radial.x);

    float broadInner = sin(angle * 7.0 - u_time * 1.18) * 0.072;
    float mediumInner = sin(angle * 13.0 + u_time * 0.84) * 0.034;
    float fineInner = sin(angle * 23.0 - u_time * 1.64) * 0.014;
    float broadOuter = sin(angle * 6.0 + u_time * 0.96 + 1.7) * 0.082;
    float mediumOuter = sin(angle * 11.0 - u_time * 1.16 + 0.8) * 0.039;
    float fineOuter = sin(angle * 19.0 + u_time * 1.48) * 0.016;

    float isHalo = step(1.5, a_surface);
    float isOuter = step(2.5, a_surface);
    float mainSide = step(0.5, a_surface) * (1.0 - isHalo);
    float mainInner = step(1.1, a_surface) * mainSide;
    float mainOuter = mainSide * (1.0 - mainInner);
    float faceWave = mix(broadInner + mediumInner + fineInner, broadOuter + mediumOuter + fineOuter, a_texCoord0.y);
    float sideWave = (broadInner + mediumInner + fineInner) * mainInner + (broadOuter + mediumOuter + fineOuter) * mainOuter;
    float haloWave = mix(broadInner + mediumInner + fineInner, broadOuter + mediumOuter + fineOuter, isOuter);
    float wave = mix(faceWave, sideWave, mainSide);
    wave = mix(wave, haloWave, isHalo);

    vec4 displaced = a_position + vec4(radial * wave, 0.0);
    vec4 worldPos = u_trans * displaced;
    v_uv = a_texCoord0;
    v_worldPos = worldPos.xyz;
    v_worldNormal = normalize((u_trans * vec4(a_normal, 0.0)).xyz);
    v_surface = a_surface;
    gl_Position = u_proj * worldPos;
}
