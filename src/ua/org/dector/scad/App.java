package ua.org.dector.scad;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import ua.org.dector.scad.model.Document;
import ua.org.dector.scad.model.Item;
import ua.org.dector.scad.model.nodes.*;

import javax.swing.*;

/**
 * @author dector
 */
public class App extends Game {
    public enum Mode { NONE, EDIT, DOWN_ARROW_INSERT }

    private Document document;
    private Renderer renderer;

    private Mode mode;
    
    private Arrow unpairedArrow;
    
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
        if (mode != Mode.EDIT
                && mode != Mode.DOWN_ARROW_INSERT) return;
        
        Item curr = document.getCurrentItem();
        Item prev = curr.getPrev();
        if (prev == null) return;

        if (append) {
            if (! document.select(prev, true))
                document.deselect(curr);
        } else
            document.selectOnly(prev);
        
        document.setCurrentItem(prev);
        setRendererDirty();
    }

    public void selectNext(boolean append) {
        if (mode != Mode.EDIT
                && mode != Mode.DOWN_ARROW_INSERT) return;

        Item curr = document.getCurrentItem();
        Item next = curr.getNext();
        if (next == null) return;

        if (append) {
            if (! document.select(next, false))
                document.deselect(curr);
        } else
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

            insertItemBetweenAndSelect(newItem, prevItem, nextItem);

            setRendererDirty();
        }
    }
    
    public void createConditionalNode(boolean enterId) {
        if (mode != Mode.EDIT) return;
        if (document.getCurrentItem().getType() == Item.Type.END) return;

        int id;
        if (enterId)
            id = enterId(Condition.getLasId() + 1);
        else
            id = Condition.nextId();

        if (id != -1) {
            Item prevItem = document.getCurrentItem();
            Item nextItem = prevItem.getNext();
            Item newItem = new Item(Item.Type.X, id);

            insertItemBetweenAndSelect(newItem, prevItem, nextItem);

            createArrow(enterId);

            setRendererDirty();
        }
    }
    
    public void createArrow(boolean enterId) {
        if (mode != Mode.EDIT) return;
        if (document.getCurrentItem().getType() == Item.Type.END) return;

        int id;
        if (enterId)
            id = enterId(Arrow.getLasId() + 1);
        else
            id = Arrow.nextId();

        if (id != -1) {
            Item prevItem = document.getCurrentItem();
            Item nextItem = prevItem.getNext();
            Item newItem = new Arrow(Item.Type.ARROW_UP, id);

            insertItemBetweenAndSelect(newItem, prevItem, nextItem);

            mode = Mode.DOWN_ARROW_INSERT;
            unpairedArrow = (Arrow)newItem;

            setRendererDirty();
        }
    }

    public void insertDownArrow() {
        if (mode != Mode.DOWN_ARROW_INSERT) return;
        
        Item currItem = document.getCurrentItem();
        if (currItem.getType() == Item.Type.BEGIN) return;
        if (currItem.getType() == Item.Type.ARROW_UP
                && currItem.getPrev().getType() == Item.Type.X) return;

        Item nextItem = document.getCurrentItem();
        Item prevItem = nextItem.getPrev();
        Item newItem = new Arrow(Item.Type.ARROW_DOWN, unpairedArrow.getId());

        insertItemBetweenAndSelect(newItem, prevItem, nextItem, false);

        unpairedArrow = null;

        mode = Mode.EDIT;

        setRendererDirty();
    }
    
    public void cancelArrowInsert() {
        if (mode != Mode.DOWN_ARROW_INSERT) return;

        if (unpairedArrow.getPrev().getType() == Item.Type.X) {
            // If it's after x -> cancel insert last x
            document.selectOnly(unpairedArrow.getPrev());
            removeItem();
        }

        // Remove last up arrow
        if (unpairedArrow.getId() == Arrow.getLasId())
            Arrow.decLastId();

        document.selectOnly(unpairedArrow);
        removeItem();

        unpairedArrow = null;

        mode = Mode.EDIT;
    }
    
    public void removeItem() {
        Item itemToRemove = document.getCurrentItem();
        
        if (itemToRemove.getType() == Item.Type.BEGIN) return;
        if (itemToRemove.getType() == Item.Type.END) return;
        
        Item item = document.getHead();
        while (item.hasNext() && itemToRemove != item) {
            item = item.getNext();
        }
        
        if (itemToRemove == item) {
            Item nextItem = item.getNext();
            Item prevItem = item.getPrev();

            prevItem.setNext(nextItem);
            nextItem.setPrev(prevItem);

            selectPrev(false);

            setRendererDirty();
        }
    }

    public void toggleGroup() {
        if (mode != Mode.EDIT) return;

        Item currItem = document.getCurrentItem();
        
        if (document.getSelectedCount() > 1) {
            boolean valid = true;
            Item[] selected = document.getSelected();
            
            int selCount = selected.length;
            Item item;

            {
                int i = 0;
                while (valid && i < selCount) {
                    item = selected[i];
    
                    if (item.getType() != Item.Type.Y
                            || ((Operational)item.getNode()).getSignalsCount() > 1) {
                        valid = false;
                    } else {
                        i++;
                    }
                }
            }

            // Group
            if (valid) {
                Signal[] signals = new Signal[selCount];
                
                for (int i = 0; i < selCount; i++) {
                    signals[i] = ((Operational)selected[i].getNode()).getSignal();
                }
                
                Operational newNode = new Operational(signals);
                Item newItem = new Item(Item.Type.Y);
                newItem.setNode(newNode);

                Item firstItem = selected[0];
                Item lastItem = selected[selCount - 1];

                insertItemBetweenAndSelect(newItem, firstItem.getPrev(), lastItem.getNext());

                document.selectOnly(newItem);

                setRendererDirty();
            }
        } else if (currItem.getType() == Item.Type.Y
                && ((Operational)currItem.getNode()).getSignalsCount() > 1) {
            // Ungroup
            Signal[] signals = ((Operational) currItem.getNode()).getSignals();
            
            Item item = null;
            Item prevItem = currItem.getPrev();
            Item nextItem = currItem.getNext();

            // For gc
            currItem.setNext(null);
            currItem.setPrev(null);

            for (Signal signal : signals) {
                item = new Item(Item.Type.Y, signal.getId());

                insertItemBetweenAndSelect(item, prevItem, nextItem);

                prevItem = item;
            }

            document.selectOnly(item);
            
            setRendererDirty();
        }
    }
    
    public void editItemId() {
        if (mode != Mode.EDIT) return;
        
        Item currItem = document.getCurrentItem();
        Item.Type currItemType = currItem.getType();
        
        if (currItemType == Item.Type.BEGIN) return;
        if (currItemType == Item.Type.END) return;
        
        if (currItemType == Item.Type.Y
                && ((Operational) currItem.getNode()).getSignalsCount() > 1) return;

        // Can't change arrow id
        /*if (currItemType == Item.Type.ARROW_DOWN
                || currItemType == Item.Type.ARROW_UP) {
            Arrow currArrow = (Arrow) currItem;
            Arrow pairArrow = currArrow.getPair();

            int newId = enterId(currArrow.getId());
            currArrow.setId(newId);
            pairArrow.setId(newId);

        } else*/ if (currItemType == Item.Type.X) {
            Condition cond = ((Conditional) currItem.getNode()).getCondition();
            cond.setId(enterId(cond.getId()));

            setRendererDirty();
        } else if (currItemType == Item.Type.Y) {
            Signal signal = ((Operational) currItem.getNode()).getSignal();
            signal.setId(enterId(signal.getId()));

            setRendererDirty();
        }
    }

    private void insertItemBetweenAndSelect(Item newItem, Item prevItem, Item nextItem) {
        insertItemBetweenAndSelect(newItem, prevItem, nextItem, true);
    }
    
    private void insertItemBetweenAndSelect(Item newItem, Item prevItem, Item nextItem, boolean selectNext) {
        newItem.setPrev(prevItem);
        newItem.setNext(nextItem);

        prevItem.setNext(newItem);
        nextItem.setPrev(newItem);

        if (selectNext)
            selectNext(false);
        else
            selectPrev(false);
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
