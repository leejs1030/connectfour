package com.leejs1030.connectfour.consts;

public interface Consts {
    int MAXROW = 6, MAXCOL = 7;
    char CHIP0 = 'O', CHIP1 = 'X', BLANK = '.';
    int AITURN = 0; // 0:선공 1: 후공
    int MAXDEPTH = 9;
    int INF = 0x6fffffff;
    
    
    int AI_ROW_TWO_SCORE = 3, AI_ROW_THREE_SCORE = 9;
    int AI_COL_TWO_SCORE = 3, AI_COL_THREE_SCORE = 9;
    int AI_DIA_TWO_SCORE = 3, AI_DIA_THREE_SCORE = 9;

    int PLAYER_ROW_TWO_SCORE = -3, PLAYER_ROW_THREE_SCORE = -9;
    int PLAYER_COL_TWO_SCORE = -3, PLAYER_COL_THREE_SCORE = -9;
    int PLAYER_DIA_TWO_SCORE = -3, PLAYER_DIA_THREE_SCORE = -9;
}
