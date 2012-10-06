package ua.org.dector.scad;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

import static com.badlogic.gdx.Input.Keys.*;

/**
 * @author dector
 */
public class InputController implements InputProcessor {
    private App app;

    private boolean shiftPressed;

    public InputController(App app) {
        this.app = app;
    }

    public boolean keyDown(int keycode) {
        boolean handled = true;

        switch (keycode) {
            case N:
                app.createDocument();
                break;
            case E:
                app.enterEditMode();
                break;
            case ESCAPE:
                app.exitEditMode();
                break;
            case LEFT:
                app.selectPrev(shiftPressed);
                break;
            case RIGHT:
                app.selectNext(shiftPressed);
                break;
            case Y:
                app.createOperationalNode(shiftPressed);
                break;
            case X:
                app.createConditionalNode(shiftPressed);
                break;
            case T:
                app.createArrow(shiftPressed);
                break;
            case SHIFT_LEFT:
            case SHIFT_RIGHT:
                shiftPressed = true;
                break;
            case ENTER:
                app.insertDownArrow();
            default:
                handled = false;
        }

        return handled;
    }

    public boolean keyUp(int keycode) {
        boolean handled = true;

        switch (keycode) {
            case SHIFT_LEFT:
            case SHIFT_RIGHT:
                shiftPressed = false;
                break;
            default:
                handled = false;
        }

        return handled;
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
