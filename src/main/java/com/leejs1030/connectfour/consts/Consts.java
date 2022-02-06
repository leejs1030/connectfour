package com.leejs1030.connectfour.consts;

import java.awt.Color;

public interface Consts {
    int MAXROW = 6, MAXCOL = 7;
    char CHIP0 = 'O', CHIP1 = 'X', BLANK = '.';
    Color CHIP0_COLOR = Color.RED, CHIP1_COLOR = Color.YELLOW, BLANK_COLOR = Color.WHITE;
    Color CONTROL_COLOR = null;
    Color[] text_colors = {CHIP0_COLOR, CHIP1_COLOR.darker()};
    int AITURN = 0; // 0:선공 1: 후공
    int MAXDEPTH = 9;
    int INF = 0x6fffffff;
    int[] insertOrder = {3, 2, 4, 1, 5, 0, 6};
    
    
    int AI_ROW_TWO_SCORE = 60, AI_ROW_THREE_SCORE = 180;
    int AI_COL_TWO_SCORE = 59, AI_COL_THREE_SCORE = 179;
    int AI_DIA_TWO_SCORE = 60, AI_DIA_THREE_SCORE = 180;

    int PLAYER_ROW_TWO_SCORE = -90, PLAYER_ROW_THREE_SCORE = -200;
    int PLAYER_COL_TWO_SCORE = -89, PLAYER_COL_THREE_SCORE = -199;
    int PLAYER_DIA_TWO_SCORE = -90, PLAYER_DIA_THREE_SCORE = -200;
}
