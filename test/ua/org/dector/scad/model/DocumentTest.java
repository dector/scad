package ua.org.dector.scad.model;

import org.junit.*;
import static org.junit.Assert.*;

/**
 * @author dector
 */
public class DocumentTest {
    private Document doc;

    @Before
    public void createDocument() throws Exception {
        doc = new Document();
    }

    @Test
    public void justCreated() throws Exception {
        // null <- Start <-> End -> null
        //            ^
        //     head --+
        
        Item head = doc.getHead();
        
        assertNotNull(head);
        assertEquals(head.getType(), Item.Type.BEGIN);
        assertNull(head.getPrev());
        
        Item postHead = head.getNext();
        
        assertNotNull(postHead);
        assertEquals(postHead.getType(), Item.Type.END);
        assertNull(postHead.getNext());
    }
}
