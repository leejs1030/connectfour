package com.leejs1030.connectfour.ai;

import com.leejs1030.connectfour.consts.Consts;
import com.leejs1030.connectfour.datastructure.Board;
import com.leejs1030.connectfour.myexception.WrongInputException;

public class AI {
    Board original = null;
    public AI(Board b){ original = b; } // shallow copy.

    public int useTurn(){
        int col = selectColumn();
        try{
            original.insertChip(col, getChip(true)); // 에러 날 가능성 없음.
        } catch(WrongInputException err){
            System.out.println(err.getMessage());
        }
        return col;
    }

    private int selectColumn(){
        int value = -Consts.INF, col = 0;
        for(int i = 0; i < Consts.MAXCOL; i++){
            Board copy = new Board(this.original);
            try{
                copy.insertChip(i, getChip(true));
            } catch(WrongInputException e){
                continue;
            }
            int res = minimax(copy, i, Consts.MAXDEPTH - 1, -Consts.INF, +Consts.INF, false);
            if(res > value){
                value = res; col = i;
            } else if(res == value){ // 같은 점수라면
                if(Math.abs(Consts.MAXCOL / 2 - i) < Math.abs(Consts.MAXCOL / 2 - col)){ // 중앙에 놓는 것을 더 선호
                    value = res; col = i;
                }
            }
        }
        return col;
        // minimax 에서 Pair를 써서 col까지 리턴하는 게 코드는 더 간편할 것. selectColumn이 아예 필요 없어짐.
        // 하지만 col값에 대한 판단을 minimax마다 고려해야한다는 단점이 존재.(현재는 selectColumn에서 마지막에만 고려)
        // 조금 더 생각해보고 수정할지 말지 결정.
    }
    
    private int minimax(Board node, int col, int depth, int alpha, int beta, boolean isAITurn){
        if(node.isFinished(node.getTop(col) - 1, col)){
            if(isAITurn) return -Consts.INF;
            else return Consts.INF;
        } else if(depth == 0){
            return evaluate(node);
        }

        if(isAITurn){ // 최대화 하기
            for(int i = 0; i < Consts.MAXCOL; i++){
                Board copy = new Board(node);
                try{
                    copy.insertChip(i, getChip(isAITurn));
                } catch(WrongInputException e){
                    continue;
                }
                alpha = Math.max(alpha, minimax(copy, i, depth - 1, alpha, beta, !isAITurn));
                if(alpha >= beta) return alpha;
            }
            return alpha;
        } else{ // 최소화 하기
            for(int i = 0; i < Consts.MAXCOL; i++){
                Board copy = new Board(node);
                try{
                    copy.insertChip(i, getChip(isAITurn));
                } catch(WrongInputException e){
                    continue;
                }
                beta = Math.min(beta, minimax(copy, i, depth - 1, alpha, beta, !isAITurn));
                if(alpha >= beta) return beta;
            }
            return beta;
        }
    }

    private int evaluate(Board b){ //주어진 보드의 점수를 리턴.
        return 1;
    }

    private char getChip(boolean isAITurn){
        if(getTurn(isAITurn) == 0) return Consts.CHIP0;
        else return Consts.CHIP1;
    }

    private int getTurn(boolean isAITurn){
        if(isAITurn) return Consts.AITURN;
        else return Consts.AITURN ^ 1;
    }
}
