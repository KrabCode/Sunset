#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

uniform vec2 resolution;
uniform float time;
uniform float mX;
uniform float mY;

vec3 colorA = vec3(1.,0.,0.);
vec3 colorB = vec3(.3,.2,.8);

void main(){
    vec2 uv = gl_FragCoord.xy / resolution.xy;
    float pct = abs(1.-uv.y*1.8);
    vec3 color = mix(colorA, colorB, pct);

    if(mod(pow(uv.y, 8) - time/52., .03)<.015){
        //color = vec3(1.-color.x, 1.-color.y,1.-color.z);
        color *= .3f;
    }

    gl_FragColor = vec4(color,1.);
}