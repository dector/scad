package ua.org.dector.scad.model;

import java.util.LinkedList;

/**
 * Lsa items holder
 *
 * @author dector
 */
public class Document {
    private Item head;
    
    private LinkedList<Item> selectedItems;
    private Item currentItem;

    /**
     * New instance
     */
    public Document() {
        selectedItems = new LinkedList<Item>();
        
        head = new Item(Item.Type.BEGIN);
        Item end = new Item(Item.Type.END);
        head.setNext(end);
        end.setPrev(head);
        
        selectOnly(head);
        setCurrentItem(head);
    }

    /**
     * Returns first item
     *
     * @return begin item
     */
    public Item getHead() {
        return head;
    }

    /**
     * Select only one item
     *
     * @param item item to select
     */
    public void selectOnly(Item item) {
        deselectAll();
        select(item, false);
        setCurrentItem(item);
    }

    /**
     * Select another item
     *
     * @param item item to select
     * @param before select before current?
     * @return true if selected
     */
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

    /**
     * Deselect item
     *
     * @param item item to deselect
     */
    public void deselect(Item item) {
        selectedItems.remove(item);
    }

    /**
     * Reset selection
     */
    public void deselectAll() {
        selectedItems.clear();
    }

    /**
     * Check if is item selected
     *
     * @param item item to check
     * @return true if selected
     */
    public boolean isSelected(Item item) {
        return selectedItems.contains(item);
    }

    /**
     * Count selected items
     *
     * @return selected items number
     */
    public int getSelectedCount() {
        return selectedItems.size();
    }

    /**
     * Returns selected items as array
     *
     * @return selected items array
     */
    public Item[] getSelected() {
        Item[] selected = new Item[getSelectedCount()];

        selectedItems.toArray(selected);

        return selected;
    }

    /**
     * Sets current item
     *
     * @param item new current item
     */
    public void setCurrentItem(Item item) {
        currentItem = item;
    }

    /**
     * Returns current item
     *
     * @return current item
     */
    public Item getCurrentItem() {
        return currentItem;
    }

    /**
     * Returns first selected item
     *
     * @return first in selected
     */
    public Item getFirstSelected() {
        return selectedItems.getFirst();
    }

    /**
     * Returns last selected item
     *
     * @return last in selected
     */
    public Item getLastSelected() {
        return selectedItems.getLast();
    }
}
