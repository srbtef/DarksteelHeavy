uniform highp float u_time;
uniform float u_alpha;
uniform vec3 u_campos;
uniform float u_lightdir;

varying vec2 v_uv;
varying vec3 v_worldPos;
varying vec3 v_worldNormal;
varying float v_surface;

void main(){
    vec3 normal = normalize(v_worldNormal);
    vec3 viewDir = normalize(u_campos - v_worldPos);
    vec3 lightDir = normalize(u_lightdir);
    vec3 radial = normalize(v_worldPos);
    vec3 halfDir = normalize(viewDir + lightDir);

    float facing = abs(dot(normal, viewDir));
    float fresnel = pow(1.0 - facing, 2.6);
    float surfaceSpecular = pow(max(dot(normal, halfDir), 0.0), 28.0);
    float arcFacing = max(dot(radial, halfDir), 0.0);
    float arcReflection = pow(arcFacing, 22.0);
    float broadReflection = pow(arcFacing, 6.0);
    float reflection = surfaceSpecular * 0.28 + arcReflection * 0.82 + broadReflection * 0.22 + fresnel * 0.22;

    float halo = step(1.5, v_surface);
    float side = step(0.5, v_surface) * (1.0 - halo);

    float core = 1.0 - smoothstep(0.0, 0.38, abs(v_uv.y - 0.5));

    float layersA = 0.5 + 0.5 * sin((v_uv.y * 46.0 + sin(v_uv.y * 11.0) * 0.23) * 6.2831853);
    float layersB = 0.5 + 0.5 * sin((v_uv.y * 73.0 - v_uv.x * 1.7 + u_time * 0.025) * 6.2831853);
    float layersC = 0.5 + 0.5 * sin((v_uv.y * 19.0 + v_uv.x * 0.8) * 6.2831853);
    float grooves = 0.18 + pow(layersA, 5.0) * 0.52 + pow(layersB, 8.0) * 0.22 + layersC * 0.08;

    float fineFlow = 0.5 + 0.5 * sin((v_uv.x * 31.0 - u_time * 0.58 + v_uv.y * 2.4) * 6.2831853);
    float chase = pow(max(0.0, sin((v_uv.x * 4.0 - u_time * 0.18) * 6.2831853)), 14.0);
    float energyPulse = 0.88 + 0.12 * sin((v_uv.x * 7.0 - u_time * 0.22) * 6.2831853);

    float emissive = (0.40 + core * 0.28 + grooves * 0.40 + fineFlow * 0.10 + chase * 0.32) * energyPulse;
    float faceAlpha = 0.36 + core * 0.20 + grooves * 0.20 + chase * 0.13 + reflection * 0.14;
    float sideAlpha = 0.28 + fresnel * 0.30 + arcReflection * 0.22;

    float haloFalloff = smoothstep(0.0, 1.0, v_uv.y);
    float haloBody = haloFalloff * haloFalloff * (3.0 - 2.0 * haloFalloff);
    float haloVariation = 0.84 + 0.16 * sin((v_uv.x * 5.0 - u_time * 0.08) * 6.2831853);
    float haloPulse = 0.90 + 0.10 * sin((v_uv.x * 2.0 + u_time * 0.11) * 6.2831853);
    float haloAlpha = haloBody * (0.16 + fresnel * 0.075 + broadReflection * 0.045) * haloVariation * haloPulse;
    float bloomSource = pow(haloFalloff, 4.0) * (1.20 + chase * 0.46) * haloPulse;

    vec3 deepGreen = vec3(0.012, 0.34, 0.24);
    vec3 energyGreen = vec3(0.04, 0.82, 0.52);
    vec3 mintGlow = vec3(0.14, 0.94, 0.64);
    vec3 reflectionColor = vec3(0.26, 0.96, 0.70);

    vec3 faceColor = mix(deepGreen, energyGreen, clamp(0.22 + core * 0.24 + grooves * 0.44 + fineFlow * 0.08, 0.0, 1.0));
    faceColor += energyGreen * emissive * 0.62;
    faceColor += mintGlow * (chase * 0.34 + fresnel * 0.18);
    faceColor += reflectionColor * reflection * 0.62;

    vec3 sideColor = energyGreen * (0.30 + fresnel * 0.62 + arcReflection * 0.55);
    vec3 haloColor = mix(vec3(0.008, 0.22, 0.16), vec3(0.045, 0.82, 0.54), haloBody);
    haloColor += vec3(0.035, 0.68, 0.46) * bloomSource * 1.50;

    vec3 color = mix(faceColor, sideColor, side);
    color = mix(color, haloColor, halo);
    float alpha = mix(mix(faceAlpha, sideAlpha, side), haloAlpha, halo) * u_alpha;

    gl_FragColor = vec4(color, alpha);
}