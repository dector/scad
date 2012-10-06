package ua.org.dector.scad;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import ua.org.dector.scad.model.Document;
import ua.org.dector.scad.model.Item;
import ua.org.dector.scad.model.nodes.Signal;

import javax.swing.*;

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
        if (mode != Mode.EDIT) return;
        
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
        if (mode != Mode.EDIT) return;

        Item next = document.getCurrentItem().getNext();
        if (next == null) return;

        if (append)
            document.select(next);
        else
            document.selectOnly(next);

        document.setCurrentItem(next);
        setRendererDirty();
    }
    
    public void createOperationalNode(boolean enterId) {
        if (mode != Mode.EDIT) return;
        if (document.getCurrentItem().getType() == Item.Type.END) return;
        
        int id;
        if (enterId)
            id = enterId(Signal.getLasId() + 1);
        else
            id = Signal.nextId();
        
        if (id != -1) {
            Item prevItem = document.getCurrentItem();
            Item nextItem = prevItem.getNext();
            Item newItem = new Item(Item.Type.Y, id);

            newItem.setPrev(prevItem);
            newItem.setNext(nextItem);

            prevItem.setNext(newItem);
            nextItem.setPrev(newItem);

            selectNext(false);

            setRendererDirty();
        }
    }
    
    private int enterId(int defaultId) {
        final int[] id = new int[1];

        String text = JOptionPane.showInputDialog(null, "Input inner signal id", String.valueOf(defaultId));

        try {
            id[0] = Integer.valueOf(text);
        } catch (NumberFormatException e) {
            id[0] = -1;
        }

        if (id[0] == Signal.getLasId() + 1)
            Signal.nextId();

        return id[0];
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
