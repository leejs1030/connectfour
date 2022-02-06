package com.leejs1030.connectfour.datastructure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import com.leejs1030.connectfour.consts.Consts;
import com.leejs1030.connectfour.myexception.WrongInputException;

import org.junit.Test;

public class BoardTest {
    @Test
    public void insertChipTest() throws WrongInputException{
        Board b = new Board();
        System.out.println("start");
        b.insertChip(0, 1);
        b.insertChip(1, 1);
        b.insertChip(0, 0);
        b.insertChip(1, 1);
        b.insertChip(1, 0);
        assertEquals(b.get(0, 0), 1);
        assertEquals(b.get(1, 0), 0);
        assertEquals(b.get(0, 1), 1);
        System.out.println("end");
        Board b1 = new Board();
        Board t = new Board();
        b1.insertChip(0, 0);
        b1.insertChip(0, 1);
        b1.insertChip(1, 0);
        b1.insertChip(3, 1);
        assertEquals(b1, b1);
        t.insertChip(0, 0);
        t.insertChip(0, 1);
        t.insertChip(1, 0);
        t.insertChip(3, 1);
        assertEquals(b1, t);
        t.insertChip(2, 0);
        assertNotEquals(b1, t);
    }

    @Test
    public void deepCopyTest() throws WrongInputException{
        Board b = new Board();
        b.insertChip(0, 'A');
        b.insertChip(1, 'B');
        b.insertChip(0, 'A');
        b.insertChip(1, 'T');
        b.insertChip(1, 'T');
        Board t = new Board(b);
        assertEquals(b, t);
        b.insertChip(5, 'X');
        assertNotEquals(b, t);
    }

    @Test
    public void getTopTest() throws WrongInputException{
        Board b = new Board();
        assertEquals(b.getTop(0), 0);
        b.insertChip(0, 'A');
        assertEquals(b.getTop(0), 1);
        b.insertChip(0, 'A');
        assertEquals(b.getTop(0), 2);
        assertEquals(b.getTop(1), 0);
        b.insertChip(3, 'A');
        assertEquals(b.getTop(3), 1);
        assertEquals(b.getTop(6), 0);
        b.insertChip(6, 'A');
        assertEquals(b.getTop(6), 1);
    }

    @Test
    public void isFinishedTest() throws WrongInputException{
        Board b = new Board();
        b.insertChip(0, Consts.CHIP0);
        b.insertChip(0, Consts.CHIP0);
        b.insertChip(0, Consts.CHIP0);
        b.insertChip(0, Consts.CHIP0);
        assertTrue(b.isFinished(0, 0));

        b = new Board();
        b.insertChip(0, Consts.CHIP0);
        b.insertChip(1, Consts.CHIP0);
        b.insertChip(2, Consts.CHIP0);
        b.insertChip(3, Consts.CHIP0);
        assertTrue(b.isFinished(0, 0));
        assertTrue(b.isFinished(0, 1));
        assertTrue(b.isFinished(0, 2));
        assertTrue(b.isFinished(0, 3));
        assertFalse(b.isFinished(0, 4));

        b = new Board();
        b.insertChip(0, Consts.CHIP0);
        b.insertChip(1, Consts.CHIP1);
        b.insertChip(1, Consts.CHIP0);
        b.insertChip(2, Consts.CHIP1);
        b.insertChip(2, Consts.CHIP1);
        b.insertChip(2, Consts.CHIP0);
        b.insertChip(3, Consts.CHIP1);
        b.insertChip(3, Consts.CHIP1);
        b.insertChip(3, Consts.CHIP1);
        b.insertChip(3, Consts.CHIP0);
        assertTrue(b.isFinished(0, 0));
        assertTrue(b.isFinished(1, 1));
        assertTrue(b.isFinished(2, 2));
        assertTrue(b.isFinished(3, 3));
        assertFalse(b.isFinished(0, 1));

        b = new Board();
        b.insertChip(3, Consts.CHIP0);
        b.insertChip(2, Consts.CHIP1);
        b.insertChip(2, Consts.CHIP0);
        b.insertChip(1, Consts.CHIP1);
        b.insertChip(1, Consts.CHIP1);
        b.insertChip(1, Consts.CHIP0);
        b.insertChip(0, Consts.CHIP1);
        b.insertChip(0, Consts.CHIP1);
        b.insertChip(0, Consts.CHIP1);
        b.insertChip(0, Consts.CHIP0);
        assertTrue(b.isFinished(3, 0));
        assertTrue(b.isFinished(2, 1));
        assertTrue(b.isFinished(1, 2));
        assertTrue(b.isFinished(0, 3));
        assertFalse(b.isFinished(0, 0));
    }

    @Test
    public void undoTest() throws WrongInputException{
        Board b = new Board();
        System.out.println("start");
        b.insertChip(0, 'A');
        b.insertChip(1, 'B');
        b.insertChip(0, 'C');
        b.insertChip(1, 'D');
        b.insertChip(1, 'E');
        assertEquals(b.get(0, 0), 'A');
        assertEquals(b.get(1, 0), 'C');
        assertEquals(b.get(0, 1), 'B');
        System.out.println("end");
        Board b1 = new Board();
        Board t = new Board();
        b1.insertChip(0, 'O');
        b1.insertChip(0, 'X');
        b1.insertChip(1, 'O');
        b1.insertChip(3, 'X');
        assertEquals(b1, b1);
        t.insertChip(0, 'O');
        t.insertChip(0, 'X');
        t.insertChip(1, 'O');
        t.insertChip(3, 'X');
        assertEquals(b1, t);
        t.insertChip(2, 'O');
        assertNotEquals(b1, t);

        assertEquals(2, t.undo());
        assertEquals(b1, t);
        assertEquals(3, t.undo());
        assertEquals(1, t.undo());
        assertEquals(0, t.undo());
        assertEquals(0, t.undo());
        assertEquals(new Board(), t);
    }

    @Test
    public void fullTest() throws WrongInputException{
        Board b = new Board();
        for(int i = 0; i < Consts.MAXROW; i++){
            for(int j = 0; j < Consts.MAXCOL; j++){
                b.insertChip(j, Consts.CHIP0);
            }
        }
        assertTrue(b.isFull());
    }
}
