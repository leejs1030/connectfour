package com.leejs1030.connectfour;

import java.util.InputMismatchException;
import java.util.Scanner;

import com.leejs1030.connectfour.consts.Consts;
import com.leejs1030.connectfour.game.GUI;
import com.leejs1030.connectfour.game.Game;

public class App 
{
    public static Scanner sc = new Scanner(System.in);
    public static void main( String[] args )
    {
        GUI gui = new GUI();
        // Game game = new Game();
        // showStartMsg();
        // int ans = getInput();
        // int winner = -1;
        // if(ans == 1){
        //     winner = game.playGame();
        // } else if(ans == 2){
        //     winner = game.playWithAI();
        // }
        // System.out.println("Player " + winner + "의 승리!");
    }

    public static void showStartMsg(){
        System.out.println("어떤 모드로 진행하시겠습니까?");
        System.out.println("1: 사람과 진행");
        System.out.println("2: AI와 진행");
    }

    public static int getInput(){
        int ans = 0;
        while(ans != 1 && ans != 2){
            try{
                ans = sc.nextInt();
            } catch(InputMismatchException err){
                sc.nextLine();
            } finally{
                System.out.println("1 또는 2를 입력해주세요!"); 
            }
        }
        return ans;
    }
}
