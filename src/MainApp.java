import processing.core.PApplet;
import processing.opengl.PShader;

public class MainApp extends PApplet{

    //based on
    //Coding Challenge #11: Terrain Generation with Perlin Noise in Processing
    // - Daniel Shiffman
    // https://www.youtube.com/watch?v=IKB1hWWedMk
    public static void main(String[] args) {
        PApplet.main("MainApp");
    }

    public void settings() {
        fullScreen(P3D, 1);
        noSmooth();
    }

    int cols, rows;
    int scl = 15;
    int w = 0;
    int h = 0;
    float[][] terrain;

    PShader sun;
    PShader sea;

    public void setup() {
//        orientation(PORTRAIT);
//        fullScreen(P3D);
        w = width*3;
        h = round(height*1.5f);
        //        noSmooth();
        colorMode(HSB);
        noFill();
        cols = w/scl;
        rows = h/scl;
        terrain = new float[cols][rows];
        for (int y = 0; y <= rows-1; y++) {
            for (int x = 0; x <= cols-1; x++) {
                terrain[x][y] = map(getNoiseAt(x, y), 0, 1, -100, 100);
            }
        }
        sun = loadShader("sun.glsl");
        sea = loadShader("sea.glsl");
    }

    public void draw() {
        background(20);

        if (frameCount%2==0) {
            //generate the sea elevation values
            float[][] newT = new float[cols][rows];
            for (int y = 0; y <= rows - 1; y++) {
                for (int x = 1; x <= cols - 1; x++) {
                    if (y == 0) {
                        newT[x][y] = map(getNoiseAt(x, y), 0, 1, -100, 100);
                    } else {
                        newT[x][y] = terrain[x][y - 1];
                    }
                }
            }
            terrain = newT;
        }

        //draw sun
        pushMatrix();
        sun.set("resolution", 800, 600); //haha sun.set, get it? haha
        sun.set("time", radians(frameCount)*.4f);
        sun.set("mX", (float)(mouseX));
        sun.set("mY", (float)(mouseY));
        shader(sun);
        fill(0);
        noStroke();
        translate(width/2, -600, -2800);
        sphere(800);
        resetShader();
        popMatrix();

        //draw sea
        pushMatrix();
        sea.set("resolution", 800, 600);
        sea.set("time", radians(frameCount));
        sea.set("mX", (float)(mouseX));
        sea.set("mY", (float)(mouseY));
//        fill(0);
//        noStroke();
//        stroke(0);
//        noFill();
        shader(sea);
        translate(width/2, height/2);
        rotateX(PI/2.5f);
        translate(-w/2, -h/2);

        //watch out for array out of bounds
        for (int y = 0; y <= rows-2; y++) {
            beginShape(TRIANGLE_STRIP);
            for (int x = 0; x <= cols-1; x++) {
                float elev = terrain[x][y];
                vertex(x*scl, y*scl, elev);
                float elev2 = terrain[x][y+1];
                vertex(x*scl, (y+1)*scl, elev2);
            }
            endShape();
        }
        popMatrix();


        //my phone only
/*
        resetShader();
        fill(150);
        noStroke();
        rect(0,0,width, 57);*/

        if(rec){
            save("sunset-" + frameCount + ".png");
        }
    }

    float getNoiseAt(float x, float y) {
        float noiseScl = map(cos(radians(frameCount/3.f)), -1,1, 0.06927548f-.02f, 0.06927548f+.02f);
        float timeScl = 2;
        return noise(x*noiseScl, y*noiseScl, timeScl*radians(frameCount));
    }

    boolean rec = false;
    @Override
    public void keyPressed() {
        super.keyPressed();
        rec = !rec;
    }


}
