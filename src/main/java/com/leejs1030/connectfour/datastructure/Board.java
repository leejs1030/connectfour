package com.leejs1030.connectfour.datastructure;

import com.leejs1030.connectfour.consts.Consts;
import com.leejs1030.connectfour.myexception.WrongInputException;

public class Board{

    private class Cell{
        private char val;
        public Cell(){this.val = '.';};
        public Cell(char val){this.val = val;};
        public Cell(Cell c) { this.val = c.val; }
    
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof Cell) return this.val == ((Cell)obj).val;
            else return this.val == ((Character)obj);
        }
    
        @Override
        public String toString(){
            return String.valueOf(this.val);
        }
    
        public char set(char val){
            this.val = val;
            return this.val;
        }
    
        public char get(){
            return this.val;
        }
    }
    
    private Cell[][] board;

    public Board(){
        this.board = new Cell[Consts.MAXROW][Consts.MAXCOL];
        initializeBoard();
    }

    public Board(Board b){
        this.board = new Cell[Consts.MAXROW][Consts.MAXCOL];
        for(int i = 0; i < Consts.MAXROW; i++){
            for(int j = 0; j < Consts.MAXCOL; j++){
                this.board[i][j] = new Cell(b.board[i][j]);
            }
        }
    }

    private void initializeBoard(){
        for(int i = 0; i < Consts.MAXROW; i++){
            for(int j = 0; j < Consts.MAXCOL; j++){
                board[i][j] = new Cell();
            }
        }
    }

    public void showBoard(){
        for(int i = Consts.MAXROW - 1; i >= 0; i--){
            System.out.print((i + 1) + " ");
            for(int j = 0; j < Consts.MAXCOL; j++){
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        System.out.print("0 ");
        for(int j = 0; j < Consts.MAXCOL; j++) System.out.print(j + 1 + " ");
        System.out.println();
    }

    public char get(int row, int col){
        return this.board[row][col].get();
    }

    public int getTop(int col){
        int r;
        for(r = Consts.MAXROW; r > 0; r--){
            if(!this.board[r - 1][col].equals('.')) break;
        }
        return r;
    }

    public int insertChip(int col, char val) throws WrongInputException {
        int r = getTop(col);
        if(r == Consts.MAXCOL) throw new WrongInputException(1);
        this.board[r][col].set(val);
        return r;
    }

    public int insertChip(int col, Cell c) throws WrongInputException {
        return insertChip(col, c.get());
    }

    @Override
    public boolean equals(Object obj) {
        Board target = (Board)obj;
        for(int i = 0; i < Consts.MAXROW; i++){
            for(int j = 0; j < Consts.MAXCOL; j++){
                if(!this.board[i][j].equals(target.board[i][j])) return false;
            }
        }
        return true;
    }
}
