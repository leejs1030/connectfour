package com.leejs1030.connectfour;

import com.leejs1030.connectfour.game.Game;

public class App 
{
    public static void main( String[] args )
    {
        Game game = new Game();
        int winner = game.playGame();
        System.out.println("Player " + winner + "의 승리!");
    }
}
