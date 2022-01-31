package com.leejs1030.connectfour.game;

import java.util.InputMismatchException;
import java.util.Scanner;

import com.leejs1030.connectfour.ai.AI;
import com.leejs1030.connectfour.consts.Consts;
import com.leejs1030.connectfour.datastructure.Board;
import com.leejs1030.connectfour.myexception.WrongInputException;

public class Game{
    private Board board;
    private int turn;
    static Scanner sc = new Scanner(System.in);

    public Game(){
        this.board = new Board();
        this.turn = 0;
    }

    public int playGame(){
        showStartMsg();
        int winner = -1;
        while(winner < 0){
            board.showBoard();
            showTurn();
            int col = useTurn();
            if(this.board.isFinished(this.board.getTop(col) - 1, col)) winner = this.turn;
            changeTurn();
        }
        board.showBoard();
        return winner;
    }

    public int playWithAI(){
        AI player0 = new AI(this.board);
        int winner = -1;
        while(winner < 0){
            board.showBoard();
            showTurn();
            if(turn == Consts.AITURN) {
                int col = player0.useTurn();
                if(this.board.isFinished(this.board.getTop(col) - 1, col)) winner = this.turn;
            } else{
                int col = useTurn();
                if(this.board.isFinished(this.board.getTop(col) - 1, col)) winner = this.turn;
            }
            changeTurn();
        }
        board.showBoard();
        return winner;
    }

    private void changeTurn(){ turn ^= 1; }

    private char getChip(){
        if(turn == 0) return Consts.CHIP0;
        else return Consts.CHIP1;
    }

    private void showStartMsg(){
        System.out.println("새 게임을 시작합니다.");
        System.out.println("Player 0: " + Consts.CHIP0);
        System.out.println("Player 1: " + Consts.CHIP1);
        System.out.print("\n\n\n");
    }

    private void showTurn(){ System.out.println("Player " + turn + " (" + getChip() + ")의 차례 입니다!"); }

    private int useTurn(){
        while(true){
            try{
                int col = getUserInput();
                this.board.insertChip(col, getChip());
                return col;
            } catch(InputMismatchException err){
                System.out.println("정수로 입력해 주세요!");
                sc.nextLine();
            }catch(WrongInputException err){
                System.out.println(err.getMessage());
            }
        }
    }

    private int getUserInput() throws WrongInputException, InputMismatchException{
        System.out.print("몇 번째 열에 놓으시겠습니까? ");
        int pos = sc.nextInt() - 1;
        if(!(0 <= pos && pos < Consts.MAXCOL)) throw new WrongInputException(0);
        // else if(this.board.isFull(pos)) throw new WrongInputException(1);
        return pos;
    }
}

