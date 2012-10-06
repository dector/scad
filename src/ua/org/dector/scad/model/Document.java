package ua.org.dector.scad.model;

import java.util.LinkedList;

/**
 * @author dector
 */
public class Document {
    private Item head;
    
    private LinkedList<Item> selectedItems;
    
    public Document() {
        selectedItems = new LinkedList<Item>();
        
        head = new Item(Item.Type.START);
        head.setNext(new Item(Item.Type.END));
    }

    public Item getHead() {
        return head;
    }

    public void selectOnly(Item item) {
        deselectAll();
        select(item);
    }

    public void select(Item item) {
        if (! isSelected(item))
            selectedItems.add(item);
    }

    public void deselect(Item item) {
        selectedItems.remove(item);
    }

    public void deselectAll() {
        selectedItems.clear();
    }

    public boolean isSelected(Item item) {
        return selectedItems.contains(item);
    }
    
    public int getSelectedSize() {
        return selectedItems.size();
    }
}
