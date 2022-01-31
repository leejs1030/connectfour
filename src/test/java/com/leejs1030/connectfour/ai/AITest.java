package com.leejs1030.connectfour.ai;

import static org.junit.Assert.assertEquals;

import com.leejs1030.connectfour.datastructure.Board;
import com.leejs1030.connectfour.myexception.WrongInputException;

import org.junit.Test;

public class AITest {
    @Test
    public void preventLoseTest() throws WrongInputException{
        Board b = new Board();
        b.insertChip(0, 'O');
        b.insertChip(1, 'X');
        b.insertChip(0, 'O');
        b.insertChip(1, 'X');
        b.insertChip(0, 'O');
        b.insertChip(0, 'X');
        b.insertChip(0, 'O');
        b.insertChip(1, 'X');
        b.showBoard();

        AI a = new AI(b);
        assertEquals(1, a.useTurn());
        b.showBoard();
    }

    @Test
    public void expectWinTest() throws WrongInputException{
        Board b = new Board();
        b.insertChip(6, 'O');
        b.insertChip(6, 'O');
        b.insertChip(6, 'O');
        b.insertChip(0, 'X');
        b.insertChip(2, 'X');
        b.insertChip(4, 'X');
        b.showBoard();

        AI a = new AI(b);
        assertEquals(6, a.useTurn());
        b.showBoard();
    }

    @Test
    public void initialTest() throws WrongInputException{
        Board b = new Board();
        AI a = new AI(b);
        a.useTurn();
    }
}
