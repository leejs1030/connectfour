package com.leejs1030.connectfour.datastructure;

import java.util.Stack;

import com.leejs1030.connectfour.consts.Consts;
import com.leejs1030.connectfour.myexception.WrongInputException;

public class Board{
    
    // private class Cell{
    //     private char val;
    //     public Cell(){this.val = Consts.BLANK;};
    //     public Cell(char val){this.val = val;};
    //     public Cell(Cell c) { this.val = c.val; }
    
    //     @Override
    //     public boolean equals(Object obj) {
    //         if(obj instanceof Cell) return this.val == ((Cell)obj).val;
    //         else return this.val == ((Character)obj);
    //     }
    
    //     @Override
    //     public String toString(){
    //         return String.valueOf(this.val);
    //     }
    
    //     public char set(char val){
    //         this.val = val;
    //         return this.val;
    //     }
    
    //     public char get(){
    //         return this.val;
    //     }
    // }
    
    // private Cell[][] board;
    
    private int[][] board;

    Stack<Integer> moveHistory = new Stack<Integer>();

    public Board(){
        this.board = new int[Consts.MAXROW][Consts.MAXCOL];
        initializeBoard();
    }

    public Board(Board b){
        this.board = new int[Consts.MAXROW][Consts.MAXCOL];
        for(int i = 0; i < Consts.MAXROW; i++){
            for(int j = 0; j < Consts.MAXCOL; j++){
                this.board[i][j] = b.board[i][j];
            }
        }
    }

    private void initializeBoard(){
        for(int i = 0; i < Consts.MAXROW; i++){
            for(int j = 0; j < Consts.MAXCOL; j++){
                board[i][j] = Consts.BLANK;
            }
        }
    }

    public void showBoard(){
        for(int i = Consts.MAXROW - 1; i >= 0; i--){
            System.out.print((i + 1) + " ");
            for(int j = 0; j < Consts.MAXCOL; j++){
                System.out.print(Consts.CHIPS[board[i][j]] + " ");
            }
            System.out.println();
        }
        System.out.print("0 ");
        for(int j = 0; j < Consts.MAXCOL; j++) System.out.print(j + 1 + " ");
        System.out.println();
    }

    public int get(int row, int col){
        return this.board[row][col];
    }

    public int getTop(int col){
        int r;
        for(r = Consts.MAXROW; r > 0; r--){
            if(this.board[r - 1][col] != (Consts.BLANK)) break;
        }
        return r;
    }

    public int insertChip(int col, int val) throws WrongInputException {
        int r = getTop(col);
        if(r == Consts.MAXROW) throw new WrongInputException(1);
        this.board[r][col] = val;
        moveHistory.push(col);
        return r;
    }

    public int undo(){
        int col = moveHistory.pop();
        popChip(col);
        return col;
    }

    private void popChip(int col){
        int r = getTop(col) - 1;
        this.board[r][col] = Consts.BLANK;
    }

    @Override
    public boolean equals(Object obj) {
        Board target = (Board)obj;
        for(int i = 0; i < Consts.MAXROW; i++){
            for(int j = 0; j < Consts.MAXCOL; j++){
                if(this.board[i][j] != target.board[i][j]) return false;
            }
        }
        return true;
    }


    public boolean isFinished(int row, int col){
        int t = board[row][col];
        if(t == Consts.BLANK) return false;
        return checkCol(row, col, t) || checkRow(row, col, t) || checkDiagonal(row, col, t);
    }

    public boolean isFull(){
        int t = Consts.BLANK;
        for(int i = 0; i < Consts.MAXCOL; i++){
            if(board[Consts.MAXROW - 1][i] == t) return false;
        }
        return true;
    }

    private boolean checkCol(int row, int col, int t){
        int cr, cl;
        for(cr = col + 1; cr < Consts.MAXCOL; cr++){
            if(board[row][cr] != t) break;
        }
        cr--;
        for(cl = col - 1; cl >= 0; cl--){
            if(board[row][cl] != t) break;
        }
        cl++;
        return (cr - cl + 1 >= 4);
    }

    private boolean checkRow(int row, int col, int t){
        int rr, rl;
        for(rr = row + 1; rr < Consts.MAXROW; rr++){
            if(board[rr][col] != t) break;
        }
        rr--;
        for(rl = row - 1; rl >= 0; rl--){
            if(board[rl][col] != t) break;
        }
        rl++;
        return (rr - rl + 1 >= 4);
    }

    private boolean checkDiagonal(int row, int col, int t){
        return checkUpperRight(row, col, t) || checkLowerRight(row, col, t);
    }

    private boolean checkUpperRight(int row, int col, int t){
        int ir, il;
        for(ir = 1; row + ir < Consts.MAXROW && col + ir < Consts.MAXCOL; ir++){
            if(board[row + ir][col + ir] != t) break;
        }
        ir--;
        for(il = -1; row + il >= 0 && col + il >= 0; il--){
            if(board[row + il][col + il] != t) break;
        }
        il++;
        return (ir - il + 1 >= 4);
    }

    private boolean checkLowerRight(int row, int col, int t){
        int ir, il;
        for(ir = 1; row + ir < Consts.MAXROW && col - ir >= 0; ir++){
            if(board[row + ir][col - ir] != t) break;
        }
        ir--;
        for(il = -1; row + il >= 0 && col - il < Consts.MAXCOL; il--){
            if(board[row + il][col - il] != t) break;
        }
        il++;
        return (ir - il + 1 >= 4);
    }
}
