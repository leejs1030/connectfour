package com.leejs1030.connectfour.ai;

import com.leejs1030.connectfour.consts.Consts;
import com.leejs1030.connectfour.datastructure.Board;
import com.leejs1030.connectfour.myexception.WrongInputException;

public class AI {
    Board original = null;
    final int AI_TURN = getTurn(true), PLAYER_TURN = getTurn(false);
    final char AI_CHIP = getChip(true), PLAYER_CHIP = getChip(false);
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
        }
        else if(node.isFull()){
            System.out.println("Full");
            return 0;
        }
        else if(depth == 0){
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
        int rowscore = rowLoop(b, ai, player), colscore = colLoop(b, ai, player), diascore = diaLoop(b, ai, player);
        score = rowscore + colscore + diascore;
        return score;
    }

    private int rowLoop(Board b, char ai, char player){
        int res = 0;
        for(int r = 0; r < Consts.MAXROW; r++){
            for(int c = 0; c < Consts.MAXCOL - 3; c++){
                res += evaluateRowAI(b, r, c);
                res += evaluateRowPlayer(b, r, c);
            }
        }
        return res;
    }

    private int evaluateRowAI(Board b, int row, int col){
        int aicount = 0, aigoodblank = 0;
        for(int i = 0; i < 4; i++){
            char cur = b.get(row, col + i);
            if(cur != Consts.BLANK){
                if(cur == AI_CHIP){
                    aicount++;
                } else if(cur == PLAYER_CHIP){
                    return 0;
                }
            } else{
                if(((row - b.getTop(col + i) - Consts.MAXDEPTH) & 1) == 0){
                    aigoodblank++;
                }
            }
        }
        int res = 0;
        if(aicount == 3){
            if(aigoodblank == 1){
                res = Consts.AI_GOOD_THREE;
            } else {
                res = Consts.AI_BAD_THREE;
            }
        } else if(aicount == 2){
            if(aigoodblank == 2){
                res = Consts.AI_GOOD_TWO;
            } else if(aigoodblank == 1){
                res = Consts.AI_TWO;
            }
            else{
                res = Consts.AI_BAD_TWO;
            }
        }
        return res;
    }

    private int evaluateRowPlayer(Board b, int row, int col){
        int playercount = 0, playergoodblank = 0;
        for(int i = 0; i < 4; i++){
            char cur = b.get(row, col + i);
            if(cur != Consts.BLANK){
                if(cur == PLAYER_CHIP){
                    playercount++;
                } else if(cur == PLAYER_CHIP){
                    return 0;
                }
            } else{
                if(((row - b.getTop(col + i) - Consts.MAXDEPTH) & 1) == 1){
                    playergoodblank++;
                }
            }
        }
        int res = 0;
        if(playercount == 3){
            if(playergoodblank == 1){
                res = Consts.PLAYER_GOOD_THREE;
            } else {
                res = Consts.PLAYER_BAD_THREE;
            }
        } else if(playercount == 2){
            if(playergoodblank == 2){
                res = Consts.PLAYER_GOOD_TWO;
            } else if(playergoodblank == 1){
                res = Consts.PLAYER_TWO;
            }
            else{
                res = Consts.PLAYER_BAD_TWO;
            }
        }
        return res;
    }

    private int colLoop(Board b, char ai, char player){
        int res = 0;
        for(int r = 0; r < Consts.MAXROW - 3; r++){
            for(int c = 0; c < Consts.MAXCOL; c++){
                res += evaluateColAI(b, r, c);
                res += evaluateColPlayer(b, r, c);
            }
        }
        return res;
    }

    private int evaluateColAI(Board b, int row, int col){
        int aicount = 0, aigoodblank = 0;
        for(int i = 0; i < 4; i++){
            char cur = b.get(row + i, col);
            if(cur != Consts.BLANK){
                if(cur == AI_CHIP){
                    aicount++;
                } else if(cur == PLAYER_CHIP){
                    return 0;
                }
            } else{
                if(((row + i - b.getTop(col) - Consts.MAXDEPTH) & 1) == 0){
                    aigoodblank++;
                }
            }
        }
        int res = 0;
        if(aicount == 3){
            if(aigoodblank == 1){
                res = Consts.AI_GOOD_THREE;
            } else {
                res = Consts.AI_BAD_THREE;
            }
        } else if(aicount == 2){
            if(aigoodblank == 2){
                res = Consts.AI_GOOD_TWO;
            } else if(aigoodblank == 1){
                res = Consts.AI_TWO;
            }
            else{
                res = Consts.AI_BAD_TWO;
            }
        }
        return res;
    }

    private int evaluateColPlayer(Board b, int row, int col){
        int playercount = 0, playergoodblank = 0;
        for(int i = 0; i < 4; i++){
            char cur = b.get(row + i, col);
            if(cur != Consts.BLANK){
                if(cur == PLAYER_CHIP){
                    playercount++;
                } else if(cur == PLAYER_CHIP){
                    return 0;
                }
            } else{
                if(((row + i - b.getTop(col) - Consts.MAXDEPTH) & 1) == 1){
                    playergoodblank++;
                }
            }
        }
        int res = 0;
        if(playercount == 3){
            if(playergoodblank == 1){
                res = Consts.PLAYER_GOOD_THREE;
            } else {
                res = Consts.PLAYER_BAD_THREE;
            }
        } else if(playercount == 2){
            if(playergoodblank == 2){
                res = Consts.PLAYER_GOOD_TWO;
            } else if(playergoodblank == 1){
                res = Consts.PLAYER_TWO;
            }
            else{
                res = Consts.PLAYER_BAD_TWO;
            }
        }
        return res;
    }

    private int diaLoop(Board b, char ai, char player){
        int res = upperRightLoop(b, ai, player) + lowerRightLoop(b, ai, player);
        return res;
    }

    private int upperRightLoop(Board b, char ai, char player){
        int res = 0;
        for(int r = 0; r < Consts.MAXROW - 3; r++){
            for(int c = 0; c < Consts.MAXCOL - 3; c++){
                res += evaluateUpperRightAI(b, r, c);
                res += evaluateUpperRightPlayer(b, r, c);
            }
        }
        return res;
    }

    private int evaluateUpperRightAI(Board b, int row, int col){
        int aicount = 0, aigoodblank = 0;
        for(int i = 0; i < 4; i++){
            char cur = b.get(row + i, col + i);
            if(cur != Consts.BLANK){
                if(cur == AI_CHIP){
                    aicount++;
                } else if(cur == PLAYER_CHIP){
                    return 0;
                }
            } else{
                if(((row + i - b.getTop(col + i) - Consts.MAXDEPTH) & 1) == 0){
                    aigoodblank++;
                }
            }
        }
        int res = 0;
        if(aicount == 3){
            if(aigoodblank == 1){
                res = Consts.AI_GOOD_THREE;
            } else {
                res = Consts.AI_BAD_THREE;
            }
        } else if(aicount == 2){
            if(aigoodblank == 2){
                res = Consts.AI_GOOD_TWO;
            } else if(aigoodblank == 1){
                res = Consts.AI_TWO;
            }
            else{
                res = Consts.AI_BAD_TWO;
            }
        }
        return res;
    }

    private int evaluateUpperRightPlayer(Board b, int row, int col){
        int playercount = 0, playergoodblank = 0;
        for(int i = 0; i < 4; i++){
            char cur = b.get(row + i, col + i);
            if(cur != Consts.BLANK){
                if(cur == PLAYER_CHIP){
                    playercount++;
                } else if(cur == PLAYER_CHIP){
                    return 0;
                }
            } else{
                if(((row + i - b.getTop(col + i) - Consts.MAXDEPTH) & 1) == 1){
                    playergoodblank++;
                }
            }
        }
        int res = 0;
        if(playercount == 3){
            if(playergoodblank == 1){
                res = Consts.PLAYER_GOOD_THREE;
            } else {
                res = Consts.PLAYER_BAD_THREE;
            }
        } else if(playercount == 2){
            if(playergoodblank == 2){
                res = Consts.PLAYER_GOOD_TWO;
            } else if(playergoodblank == 1){
                res = Consts.PLAYER_TWO;
            }
            else{
                res = Consts.PLAYER_BAD_TWO;
            }
        }
        return res;
    }

    private int lowerRightLoop(Board b, char ai, char player){
        int res = 0;
        for(int r = 3; r < Consts.MAXROW; r++){
            for(int c = 0; c < Consts.MAXCOL - 3; c++){
                res += evaluateLowerRightAI(b, r, c);
                res += evaluateLowerRightPlayer(b, r, c);
            }
        }
        return res;
    }

    private int evaluateLowerRightAI(Board b, int row, int col){
        int aicount = 0, aigoodblank = 0;
        for(int i = 0; i < 4; i++){
            char cur = b.get(row - i, col + i);
            if(cur != Consts.BLANK){
                if(cur == AI_CHIP){
                    aicount++;
                } else if(cur == PLAYER_CHIP){
                    return 0;
                }
            } else{
                if(((row - i - b.getTop(col + i) - Consts.MAXDEPTH) & 1) == 0){
                    aigoodblank++;
                }
            }
        }
        int res = 0;
        if(aicount == 3){
            if(aigoodblank == 1){
                res = Consts.AI_GOOD_THREE;
            } else {
                res = Consts.AI_BAD_THREE;
            }
        } else if(aicount == 2){
            if(aigoodblank == 2){
                res = Consts.AI_GOOD_TWO;
            } else if(aigoodblank == 1){
                res = Consts.AI_TWO;
            }
            else{
                res = Consts.AI_BAD_TWO;
            }
        }
        return res;
    }

    private int evaluateLowerRightPlayer(Board b, int row, int col){
        int playercount = 0, playergoodblank = 0;
        for(int i = 0; i < 4; i++){
            char cur = b.get(row - i, col + i);
            if(cur != Consts.BLANK){
                if(cur == PLAYER_CHIP){
                    playercount++;
                } else if(cur == PLAYER_CHIP){
                    return 0;
                }
            } else{
                if(((row - i - b.getTop(col + i) - Consts.MAXDEPTH) & 1) == 1){
                    playergoodblank++;
                }
            }
        }
        int res = 0;
        if(playercount == 3){
            if(playergoodblank == 1){
                res = Consts.PLAYER_GOOD_THREE;
            } else {
                res = Consts.PLAYER_BAD_THREE;
            }
        } else if(playercount == 2){
            if(playergoodblank == 2){
                res = Consts.PLAYER_GOOD_TWO;
            } else if(playergoodblank == 1){
                res = Consts.PLAYER_TWO;
            }
            else{
                res = Consts.PLAYER_BAD_TWO;
            }
        }
        return res;
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
