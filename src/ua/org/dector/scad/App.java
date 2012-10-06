package ua.org.dector.scad;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import ua.org.dector.scad.model.Document;

/**
 * @author dector
 */
public class App extends Game {
    private Document document;
    private Renderer renderer;
    
    public void create() {
        renderer = new Renderer(this);

        Gdx.input.setInputProcessor(new InputController(this));
    }
    
    public void createDocument() {
        if (document == null) 
            document = new Document();
    }

    public void render() {
        if (document != null)
            renderer.render(document);
    }

    public Document getDocument() {
        return document;
    }
}
