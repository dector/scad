package ua.org.dector.scad;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

import static com.badlogic.gdx.Input.Keys.*;

/**
 * @author dector
 */
public class InputController implements InputProcessor {
    private App app;

    public InputController(App app) {
        this.app = app;
    }

    public boolean keyDown(int keycode) {
        boolean handled = true;

        switch (keycode) {
            case N: app.createDocument(); break;
            case E: app.enterEditMode(); break;
            case ESCAPE: app.exitEditMode(); break;
            default: handled = false;
        }

        return handled;
    }

    public boolean keyUp(int keycode) {
        return false;
    }

    public boolean keyTyped(char character) {
        return false;
    }

    public boolean touchDown(int x, int y, int pointer, int button) {
        return false;
    }

    public boolean touchUp(int x, int y, int pointer, int button) {
        return false;
    }

    public boolean touchDragged(int x, int y, int pointer) {
        return false;
    }

    public boolean mouseMoved(int x, int y) {
        return false;
    }

    public boolean scrolled(int amount) {
        return false;
    }
}
