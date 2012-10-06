package ua.org.dector.scad;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ua.org.dector.scad.model.Document;
import ua.org.dector.scad.model.Item;

/**
 * @author dector
 */
public class Renderer {
    private static final int FONTS_COUNT    = 2;
    private static final int FONT_DEFAULT   = 0;
    private static final int FONT_SELECTED  = 1;
    
    private App app;
    
    private SpriteBatch sb;
    private BitmapFont[] fonts;
    
    private int width;
    private int height;

    private boolean dirty;
    
    public Renderer(App app, int width, int height) {
        this.app = app;
        
        this.width = width;
        this.height = height;

        setDirty(true);
        
        sb = new SpriteBatch();
        initFonts();
    }
    
    private void initFonts() {
        fonts = new BitmapFont[FONTS_COUNT];
        
        fonts[FONT_DEFAULT] = new BitmapFont();
        
        fonts[FONT_SELECTED] = new BitmapFont();
        fonts[FONT_SELECTED].setColor(Color.RED);
    }
    
    public void render(Document doc) {
        if (! dirty) return;

        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        
        sb.begin();
        
        Item item = doc.getHead();
        boolean editMode = (app.getMode() == App.Mode.EDIT);
        
        int x = 10;
        int y = 300;
        String itemStr;
        BitmapFont font;
        
        while (item != null) {
            if (editMode && doc.isSelected(item))
                font = fonts[FONT_SELECTED];
            else
                font = fonts[FONT_DEFAULT];
            
            itemStr = item.toString() + " ";
            font.draw(sb, itemStr, x, y);
            
            item = item.getNext();
            x += font.getBounds(itemStr).width;
        }
        
        sb.end();

        setDirty(false);
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }
}
