package com.leejs1030.connectfour.game;

import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.InvocationTargetException;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.*;

import com.leejs1030.connectfour.ai.AI;
import com.leejs1030.connectfour.consts.Consts;
import com.leejs1030.connectfour.datastructure.Board;
import com.leejs1030.connectfour.myexception.WrongInputException;

public class GUI extends Game implements ActionListener{
    Board board;
    private Slot[][] slots = new Slot[Consts.MAXROW][Consts.MAXCOL];
    JButton newGame1 = new JButton("vs Player");
    JButton newGame2 = new JButton("vs AI");
    JLabel player = new JLabel("Player 1's turn");
    private int winner = -1;
    private boolean aimode = true; // true: play with ai        false: paly with person
    private boolean isDraw = false;
    AI player0;
    JFrame frame;

    public GUI(){
        super();
        newGame();
        frame = new JFrame();
        frame.setBounds(0, 0, 600, 600);
        frame.setLayout(new BorderLayout());
        
        JPanel controPanel = new JPanel();
        
        controPanel.setLayout(new FlowLayout());
        controPanel.setBackground(Consts.CONTROL_COLOR);
        newGame1.addActionListener(this); newGame2.addActionListener(this);
        controPanel.add(newGame1); controPanel.add(newGame2); controPanel.add(player);

        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(Consts.MAXROW, Consts.MAXCOL));
        for(int i = Consts.MAXROW - 1; i >= 0; i--){
            for(int j = 0; j < Consts.MAXCOL; j++){
                slots[i][j] = new Slot(i, j);
                slots[i][j].addActionListener(this);
                boardPanel.add(slots[i][j]);
            }
        }

        frame.add(controPanel, BorderLayout.NORTH);
        frame.add(boardPanel, BorderLayout.CENTER);
    }

    public void show(){
        frame.setVisible(true);
        playWithAI();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    @Override
    public void newGame(){
        this.board = new Board();
        this.turn = 0;
        this.winner = -1;
    }

    @Override
    public int playGame(){
        newGame();
        updateBoard();
        showTurn();
        aimode = false;
        return 0;
    }

    @Override
    public int playWithAI(){
        newGame();
        updateBoard();
        showTurn();
        aimode = true;
        player0 = new AI(board);
        if(Consts.AITURN == 0){    
            int col = player0.useTurn();
            int row = board.getTop(col) - 1;
            // slots[row][col].setColor(getChip());
            updateBoard();
            if(board.isFinished(row, col)) winner = turn;
            changeTurn();
            showTurn();
        }
        return 0;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == newGame1) playGame();
        else if(e.getSource() == newGame2) playWithAI();
        else if(winner < 0 && !isDraw){
            final Slot btn = (Slot)e.getSource();

            
            int col = btn.getCol();
            try{
                board.insertChip(col, getChip());
            } catch(WrongInputException err){ return; } // 잘못된 입력이면 컷
            int row = board.getTop(col) - 1;
            // slots[row][col].setColor(getChip());
            updateBoard();
            System.out.println((row + 1) + "열" + (col + 1) + "행");
            if(board.isFinished(row, col)) winner = turn;
            else if(board.isFull()) isDraw = true;
            else if(aimode){
                changeTurn(); // AI의 차례
                showTurn();
                SwingUtilities.invokeLater(new Runnable(){
                    public void run(){
                        int col = player0.useTurn();
                        int row = board.getTop(col) - 1;
                        // slots[row][col].setColor(getChip());
                        updateBoard();
                        System.out.println("AI의 수: " + (row + 1) + "열" + (col + 1) + "행\n\n");
                        if(board.isFinished(row, col)) winner = turn;
                        else if(board.isFull()) isDraw = true;
                        changeTurn();
                        showTurn();
                    }
                });
            } else {
                changeTurn();
                showTurn();
            }
        }
    }

    
    public void updateBoard(){
        board.showBoard();
        for(int i = 0; i < Consts.MAXROW; i++){
            for(int j = 0; j < Consts.MAXCOL; j++){
                slots[i][j].setColor(board.get(i, j));
            }
        }
    }

    @Override
    public void showTurn(){
        if(isDraw) player.setText("Draw!");
        else if(winner < 0){
            player.setText("Player " + turn + "'s turn!");
            player.setForeground(Consts.text_colors[turn]);
        }
        else{
            if(aimode && winner == Consts.AITURN) player.setText("AI win!");
            else player.setText("Player " + winner + "win!");
            player.setForeground(Consts.text_colors[winner]);
        }
    }
}

class Slot extends JButton{
    int row, col;
    public Slot(int r, int c){
        super((r + 1) + "열" + (c + 1) + "행");
        row = r; col = c;
        setBorder(new LineBorder(Color.BLACK));
        setColor(Consts.BLANK);
    }

    public void setColor(char chip){
        switch(chip){
        case Consts.BLANK:
            setBackground(Consts.BLANK_COLOR);
            break;
        case Consts.CHIP0:
            setBackground(Consts.CHIP0_COLOR);
            break;
        case Consts.CHIP1:
            setBackground(Consts.CHIP1_COLOR);
            break;
        }
    }

    public int getRow(){ return row; }
    public int getCol(){ return col; }
}
