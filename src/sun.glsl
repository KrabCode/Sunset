uniform vec2 resolution;
uniform float time;
uniform float mX;
uniform float mY;

vec3 colorA = vec3(1.,0.,0.);
vec3 colorB = vec3(0.,0.,1.);

void main(){
    vec2 uv = gl_FragCoord.xy / resolution.xy;
    float pct = abs(.4-uv.y*1.1);
    vec3 color = mix(colorA, colorB, pct);

    if(mod(pow(uv.y, 6.8) - time/52., .03)<.01){
        //color = vec3(1.-color.x, 1.-color.y,1.-color.z);
        color *= .1f;
    }

    gl_FragColor = vec4(color,1.);
}