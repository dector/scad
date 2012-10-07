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
        select(item, false);
        setCurrentItem(item);
    }

    public boolean select(Item item, boolean before) {
        if (! isSelected(item)) {
            if (before)
                selectedItems.addFirst(item);
            else
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
    
    public int getSelectedCount() {
        return selectedItems.size();
    }

    public Item[] getSelected() {
        Item[] selected = new Item[getSelectedCount()];

        selectedItems.toArray(selected);

        return selected;
    }

    public void setCurrentItem(Item item) {
        currentItem = item;
    }

    public Item getCurrentItem() {
        return currentItem;
    }
}
