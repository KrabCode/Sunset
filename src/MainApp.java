import processing.core.PApplet;
import processing.opengl.PShader;

public class MainApp extends PApplet{
    int cols, rows;
    int scl = 25;
    int w = 3500;
    int h = 1500;
    float[][] terrain;
    PShader sun;
    PShader sea;

    public static void main(String[] args) {
        PApplet.main("MainApp");
    }

    public void settings() {
        fullScreen(P3D, 1);
    }

    public void setup() {
        colorMode(HSB);
        noFill();
        cols = w/scl;
        rows = h/scl;
        terrain = new float[cols][rows];
        for(int y = 0; y <= rows-1; y++) {
            for(int x = 0; x <= cols-1; x++){
                terrain[x][y] = map(getNoiseAt(x,y), 0, 1, -100, 100);
            }
        }
        sun = loadShader("sun.glsl");
        sea = loadShader("sea.glsl");
    }

    public void draw() {
        background(0, 1);

        //generate the sea elevation values
        if(frameCount%2==0){
            float [][] newT = new float[cols][rows];
            for(int y = 0; y <= rows-1; y++) {
                    for(int x =1; x <= cols-1; x++){
                        if(y == 0){
                            newT[x][y] = map(getNoiseAt(x,y), 0, 1, -100, 100);
                        }else{
                            newT[x][y] = terrain[x][y-1];
                        }
                    }
                }
            terrain = newT;
        }

        //draw stars
        pushMatrix();
//        rect();
        popMatrix();

        //draw sun
        pushMatrix();
        shader(sun);
        sun.set("resolution", 1920, 1080);
        sun.set("time", radians(frameCount));
        sun.set("mX", (float)(mouseX));
        sun.set("mY", (float)(mouseY));

        sea.set("resolution", 1920, 1080);
        sea.set("time", radians(frameCount));
        sea.set("mX", (float)(mouseX));
        sea.set("mY", (float)(mouseY));
        fill(0,255,255);
        noStroke();
        translate(width/2,-400,-2800);
        sphere(500);
        resetShader();
        popMatrix();

        pushMatrix();
        //sea draw
        shader(sea);
        noFill();
        stroke(0);
        translate(width/2, height/2);
        rotateX(PI/2.5f);
        translate(-w/2,-h/2);

        //watch out for array out of bounds
        for(int y = 0; y <= rows-2; y++) {
            beginShape(TRIANGLE_STRIP);
            for(int x = 0; x <= cols-1; x++){
                float elev = terrain[x][y];
                vertex(x*scl, y*scl, elev);
                float elev2 = terrain[x][y+1];
                vertex(x*scl, (y+1)*scl, elev2);
            }
            endShape();
        }
        popMatrix();
    }

    float getNoiseAt(float x, float y) {
        float noiseScl =0.06927548f; //map(mouseX, 0, width, 0.000005f, 1f);
        float timeScl = 10.53125f;//map(mouseY, 0, width, 0f, 20);
        //println(noiseScl +":" +timeScl);
        return noise(x*noiseScl, y*noiseScl, timeScl*radians(frameCount)/2);
    }

}
