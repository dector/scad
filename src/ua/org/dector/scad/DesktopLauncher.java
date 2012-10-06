package ua.org.dector.scad;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

/**
 * @author dector
 */
public class DesktopLauncher {
    private static final int SCREEN_WIDTH   = 800;
    private static final int SCREEN_HEIGHT  = 600;

    public static void main(String[] args) {
        new LwjglApplication(new App(), "SCAD", SCREEN_WIDTH, SCREEN_HEIGHT, false);
    }
}
