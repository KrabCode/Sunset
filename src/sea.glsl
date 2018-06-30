#define PROCESSING_LINE_SHADER
uniform vec2 resolution;
uniform float time;
uniform float mX;
uniform float mY;
vec3 colorA = vec3(1, 0, 0);
vec3 colorB = vec3(.3,.2,.8);
void main(){
    vec2 uv = gl_FragCoord.xy / resolution.xy;
    float pct = distance(vec2(uv.x, uv.y), vec2(0.5,1.));
    vec3 color = mix(colorA, colorB, pct*1.3);
    
    gl_FragColor = vec4(color,.4);
}