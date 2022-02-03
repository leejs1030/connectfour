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
        int alpha = -Consts.INF, col = 0, beta = Consts.INF;
        for(int j = 0; j < Consts.MAXCOL; j++){
            int i = Consts.insertOrder[j];
            try{
                original.insertChip(i, getChip(true));
            } catch(WrongInputException e){
                continue;
            }
            int res = minimax(original, i, Consts.MAXDEPTH - 1, alpha, beta, false);
            original.undo();
            if(res > alpha){
                alpha = res; col = i;
            }
        }
        return col;
        // minimax 에서 Pair를 써서 col까지 리턴하는 게 코드는 더 간편할 것. selectColumn이 아예 필요 없어짐.
        // 하지만 col값에 대한 판단을 minimax마다 고려해야한다는 단점이 존재.(현재는 selectColumn에서 마지막에만 고려)
        // 조금 더 생각해보고 수정할지 말지 결정.
    }
    
    private int minimax(Board node, int col, int depth, int alpha, int beta, boolean isAITurn){
        if(node.isFinished(node.getTop(col) - 1, col)){
            if(isAITurn) return -Consts.INF + Consts.MAXDEPTH - depth; // 늦게 지기 선택
            else return Consts.INF - Consts.MAXDEPTH + depth; // 빨리 이기기 선택
        } else if(depth == 0){
            return evaluate(node);
        }

        if(isAITurn){ // 최대화 하기
            for(int j = 0; j < Consts.MAXCOL; j++){
                int i = Consts.insertOrder[j];
                try{
                    original.insertChip(i, getChip(isAITurn));
                } catch(WrongInputException e){
                    continue;
                }
                alpha = Math.max(alpha, minimax(original, i, depth - 1, alpha, beta, !isAITurn));
                original.undo();
                if(alpha >= beta) return alpha;
            }
            return alpha;
        } else{ // 최소화 하기
            for(int j = 0; j < Consts.MAXCOL; j++){
                int i = Consts.insertOrder[j];
                try{
                    original.insertChip(i, getChip(isAITurn));
                } catch(WrongInputException e){
                    continue;
                }
                beta = Math.min(beta, minimax(original, i, depth - 1, alpha, beta, !isAITurn));
                original.undo();
                if(alpha >= beta) return beta;
            }
            return beta;
        }
    }

    private int evaluate(Board b){ //주어진 보드의 점수를 리턴.
        int score = 0;
        char ai = getChip(true), player = getChip(false);
        int rowscore = evaluateRow(b, ai, player), colscore = evaluateCol(b, ai, player), diascore = evaluateDia(b, ai, player);
        score = rowscore + colscore + diascore;
        return score;
    }

    private int evaluateRow(Board b, char ai, char player){
        int res = 0;
        for(int r = 0; r < Consts.MAXROW; r++){
            for(int c = 0; c < Consts.MAXCOL - 3; c++){
                int ca = countRow(b, r, c, ai), cp = countRow(b, r, c, player);
                if(ca == 0){
                    if(cp == 2) res += Consts.PLAYER_ROW_TWO_SCORE;
                    if(cp == 3) res += Consts.PLAYER_ROW_THREE_SCORE;
                }
                if(cp == 0){
                    if(ca == 2) res += Consts.AI_ROW_TWO_SCORE;
                    if(ca == 3) res += Consts.AI_ROW_THREE_SCORE;
                }
            }
        }
        return res;
    }

    private int countRow(Board b, int row, int col, char t){
        int count = 0;
        for(int i = 0; i < 4; i++){
            if(b.get(row, col + i) == t) count++;
        }
        return count;
    }

    private int evaluateCol(Board b, char ai, char player){
        int res = 0;
        for(int r = 0; r < Consts.MAXROW - 3; r++){
            for(int c = 0; c < Consts.MAXCOL; c++){
                int ca = countCol(b, r, c, ai), cp = countCol(b, r, c, player);
                if(ca == 0){
                    if(cp == 2) res += Consts.PLAYER_COL_TWO_SCORE;
                    if(cp == 3) res += Consts.PLAYER_COL_THREE_SCORE;
                }
                if(cp == 0){
                    if(ca == 2) res += Consts.AI_COL_TWO_SCORE;
                    if(ca == 3) res += Consts.AI_COL_THREE_SCORE;
                }
            }
        }
        return res;
    }

    private int countCol(Board b, int row, int col, char t){
        int count = 0;
        for(int i = 0; i < 4; i++){
            if(b.get(row + i, col) == t) count++;
        }
        return count;
    }

    private int evaluateDia(Board b, char ai, char player){
        int res = evaluateUpperRight(b, ai, player) + evaluateLowerRight(b, ai, player);
        return res;
    }

    private int evaluateUpperRight(Board b, char ai, char player){
        int res = 0;
        for(int r = 0; r < Consts.MAXROW - 3; r++){
            for(int c = 0; c < Consts.MAXCOL - 3; c++){
                int ca = countUpperRight(b, r, c, ai), cp = countUpperRight(b, r, c, player);
                if(ca == 0){
                    if(cp == 2) res += Consts.PLAYER_DIA_TWO_SCORE;
                    if(cp == 3) res += Consts.PLAYER_DIA_THREE_SCORE;
                }
                if(cp == 0){
                    if(ca == 2) res += Consts.AI_DIA_TWO_SCORE;
                    if(ca == 3) res += Consts.AI_DIA_THREE_SCORE;
                }
            }
        }
        return res;
    }

    private int countUpperRight(Board b, int row, int col, char t){
        int count = 0;
        for(int i = 0; i < 4; i++){
            if(b.get(row + i, col + i) == t) count++;
        }
        return count;
    }

    private int evaluateLowerRight(Board b, char ai, char player){
        int res = 0;
        for(int r = 3; r < Consts.MAXROW; r++){
            for(int c = 0; c < Consts.MAXCOL - 3; c++){
                int ca = countLowerRight(b, r, c, ai), cp = countLowerRight(b, r, c, player);
                if(ca == 0){
                    if(cp == 2) res += Consts.PLAYER_DIA_TWO_SCORE;
                    if(cp == 3) res += Consts.PLAYER_DIA_THREE_SCORE;
                }
                if(cp == 0){
                    if(ca == 2) res += Consts.AI_DIA_TWO_SCORE;
                    if(ca == 3) res += Consts.AI_DIA_THREE_SCORE;
                }
            }
        }
        return res;
    }

    private int countLowerRight(Board b, int row, int col, char t){
        int count = 0;
        for(int i = 0; i < 4; i++){
            if(b.get(row - i, col + i) == t) count++;
        }
        return count;
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
