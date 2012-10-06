package ua.org.dector.scad.model;

/**
 * @author dector
 */
public class Document {
    private Item head;
    
    public Document() {
        head = new Item(Item.Type.START);
        head.setNext(new Item(Item.Type.END));
    }

    public Item getHead() {
        return head;
    }
}
