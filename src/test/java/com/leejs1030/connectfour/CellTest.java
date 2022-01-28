package com.leejs1030.connectfour;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class CellTest {
    @Test
    public void cellEqualsTest(){
        assertEquals(new Cell('a'), 'a');
        assertEquals(new Cell('a'), new Cell('a'));
        assertNotEquals(new Cell('a'), 'b');
        assertNotEquals(new Cell('a'), new Cell('b'));
    }

    @Test
    public void cellSetTest(){
        Cell temp = new Cell(), a = new Cell('a');
        assertEquals(a, temp.set("a"));
        assertEquals(a, temp.set('a'));
    }

    @Test
    public void cloneTest(){
        Cell a = new Cell('a');
        Cell temp = new Cell(a);
        assertEquals(a, temp);
        temp.set('b');
        assertNotEquals(a, temp);
    }
}
