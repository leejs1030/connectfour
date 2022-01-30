package com.leejs1030.connectfour.ai;

import com.leejs1030.connectfour.consts.Consts;
import com.leejs1030.connectfour.datastructure.Board;
import com.leejs1030.connectfour.myexception.WrongInputException;

public class AI {
    static final int INF = 0x7fffffff;
    Board original = null;
    public AI(Board b){ original = b; } // shallow copy.

    public int useTrun() throws WrongInputException{
        int col = selectColumn();
        original.insertChip(col, getChip(true)); // 에러 날 가능성 없음.
        return col;
    }

    private int selectColumn(){
        int value = -INF, col = -1;
        for(int i = 0; i < Consts.MAXCOL; i++){
            Board copy = new Board(this.original);
            try{
                copy.insertChip(i, getChip(true));
            } catch(WrongInputException e){
                continue;
            }
            int res = minimax(copy, 10, -INF, +INF, false);
            if(res > value){
                value = res; col = i;
            }
        }
        return col;
    }
    
    private int minimax(Board node, int depth, int alpha, int beta, boolean isAITurn){
        return 1;
    }

    private int evaluate(Board b){ //주어진 보드의 점수를 리턴.
        return 1;
    }

    private char getChip(boolean isAITurn){
        if(isAITurn) return 'O';
        else return 'X';
    }
}
