package ua.org.dector.scad;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import ua.org.dector.scad.model.Document;

/**
 * @author dector
 */
public class App extends Game {
    public enum Mode { NONE, EDIT }

    private Document document;
    private Renderer renderer;

    private Mode mode;
    
    public void create() {
        renderer = new Renderer(this, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        mode = Mode.NONE;

        Gdx.input.setInputProcessor(new InputController(this));
    }
    
    public void createDocument() {
        if (document == null) 
            document = new Document();

        setRendererDirty();
    }

    public void enterEditMode() {
        if (mode == Mode.EDIT) return;

        mode = Mode.EDIT;
        if (document.getSelectedSize() == 0)
            document.selectOnly(document.getHead());

        setRendererDirty();
    }

    public void exitEditMode() {
        if (mode != Mode.EDIT) return;

        mode = Mode.NONE;

        setRendererDirty();
    }

    public void render() {
        if (document != null)
            renderer.render(document);
    }

    public Document getDocument() {
        return document;
    }

    public Mode getMode() {
        return mode;
    }

    public void setRendererDirty() {
        renderer.setDirty(true);
    }
}
