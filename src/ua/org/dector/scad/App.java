package ua.org.dector.scad;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import ua.org.dector.scad.model.Document;
import ua.org.dector.scad.model.Item;

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

        setRendererDirty();
    }

    public void exitEditMode() {
        if (mode != Mode.EDIT) return;

        mode = Mode.NONE;

        setRendererDirty();
    }

    public void selectPrev(boolean append) {
        Item prev = document.getCurrentItem().getPrev();
        if (prev == null) return;
        
        if (append)
            document.select(prev);
        else
            document.selectOnly(prev);
        
        document.setCurrentItem(prev);
        setRendererDirty();
    }

    public void selectNext(boolean append) {
        Item next = document.getCurrentItem().getNext();
        if (next == null) return;

        if (append)
            document.select(next);
        else
            document.selectOnly(next);

        document.setCurrentItem(next);
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
