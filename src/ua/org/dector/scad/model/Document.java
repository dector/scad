package ua.org.dector.scad.model;

import java.util.LinkedList;

/**
 * @author dector
 */
public class Document {
    private Item head;
    
    private LinkedList<Item> selectedItems;
    private Item currentItem;

    public Document() {
        selectedItems = new LinkedList<Item>();
        
        head = new Item(Item.Type.BEGIN);
        Item end = new Item(Item.Type.END);
        head.setNext(end);
        end.setPrev(head);
        
        selectOnly(head);
        setCurrentItem(head);
    }

    public Item getHead() {
        return head;
    }

    public void selectOnly(Item item) {
        deselectAll();
        select(item);
    }

    public boolean select(Item item) {
        if (! isSelected(item)) {
            selectedItems.add(item);
            return true;
        } else
            return false;
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
    
    public void setCurrentItem(Item item) {
        currentItem = item;
    }

    public Item getCurrentItem() {
        return currentItem;
    }
}
