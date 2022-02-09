package com.leejs1030.connectfour.game;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.LineBorder;

import com.leejs1030.connectfour.ai.AI;
import com.leejs1030.connectfour.consts.Consts;
import com.leejs1030.connectfour.datastructure.Board;
import com.leejs1030.connectfour.myexception.WrongInputException;

public class GUI extends Game implements ActionListener{
    Board board;
    private Slot[][] slots = new Slot[Consts.MAXROW][Consts.MAXCOL];
    JButton undoBtn = new JButton("undo");
    JButton newGame1Btn = new JButton("vs Player");
    JButton newGame2Btn = new JButton("vs AI");
    JLabel player = new JLabel("Player 1's turn");
    private int winner = -1;
    private boolean isDraw = false;
    AI AIPlayer;
    JFrame frame;

    public GUI(){
        super();
        newGame();
        frame = new JFrame();
        frame.setBounds(0, 0, 600, 600);
        frame.setLayout(new BorderLayout());
        
        JPanel controlPanel = new JPanel();
        
        controlPanel.setLayout(new FlowLayout());
        controlPanel.setBackground(Consts.CONTROL_COLOR);
        undoBtn.addActionListener(this); newGame1Btn.addActionListener(this); newGame2Btn.addActionListener(this);
        controlPanel.add(undoBtn); controlPanel.add(newGame1Btn); controlPanel.add(newGame2Btn); controlPanel.add(player);

        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(Consts.MAXROW, Consts.MAXCOL));
        for(int i = Consts.MAXROW - 1; i >= 0; i--){
            for(int j = 0; j < Consts.MAXCOL; j++){
                slots[i][j] = new Slot(i, j);
                slots[i][j].addActionListener(this);
                boardPanel.add(slots[i][j]);
            }
        }

        frame.add(controlPanel, BorderLayout.NORTH);
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
        AIPlayer = null;
        return 0;
    }

    @Override
    public int playWithAI(){
        newGame();
        updateBoard();
        AIPlayer = new AI(board);
        if(Consts.AITURN == 0){    
            int col = AIPlayer.useTurn();
            int row = board.getTop(col) - 1;
            // slots[row][col].setColor(getChip());
            if(board.isFinished(row, col)) winner = turn;
            changeTurn();
            updateBoard();
        }
        return 0;
    }
    
    private void undo(){
        if(AIPlayer != null && board.getStackSize() >= 2){
            System.out.println("hello?");
            winner = -1;
            board.undo(); changeTurn();
            board.undo(); changeTurn(); // chnageTurn()이 두번이라 의미는 없지만 일단 넣음.
            updateBoard();
        } else if(AIPlayer == null && board.getStackSize() >= 1){
            winner = -1;
            board.undo(); changeTurn();
            updateBoard();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == undoBtn) undo();
        else if(e.getSource() == newGame1Btn) playGame();
        else if(e.getSource() == newGame2Btn) playWithAI();
        else if(winner < 0 && !isDraw){
            final Slot btn = (Slot)e.getSource();
            int col = btn.getCol();
            try{
                board.insertChip(col, getChip());
            } catch(WrongInputException err){ return; } // 잘못된 입력이면 컷
            int row = board.getTop(col) - 1;
            System.out.println((row + 1) + "열" + (col + 1) + "행");
            if(board.isFinished(row, col)) winner = turn;
            else if(board.isFull()) isDraw = true;
            changeTurn(); // 차례 넘겨 주기
            if(AIPlayer != null && winner < 0 && !isDraw){
                updateBoard();
                SwingUtilities.invokeLater(new Runnable(){
                    public void run(){
                        int col = AIPlayer.useTurn();
                        int row = board.getTop(col) - 1;
                        System.out.println("AI의 수: " + (row + 1) + "열" + (col + 1) + "행\n\n");
                        if(board.isFinished(row, col)) winner = turn;
                        else if(board.isFull()) isDraw = true;
                        changeTurn();
                        updateBoard();
                    }
                });
            }
        }
        updateBoard();
    }

    
    public void updateBoard(){
        board.showBoard();
        for(int i = 0; i < Consts.MAXROW; i++){
            for(int j = 0; j < Consts.MAXCOL; j++){
                slots[i][j].setColor(board.get(i, j));
            }
        }
        showTurn();
    }

    @Override
    protected void showTurn(){
        if(isDraw) player.setText("Draw!");
        else if(winner < 0){
            player.setText("Player " + turn + "'s turn!");
            player.setForeground(Consts.text_colors[turn]);
        }
        else{
            player.setText("Player " + winner + "win!");
            if(AIPlayer != null && winner == Consts.AITURN) player.setText("AI win!");
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

    public void setColor(int chip){
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
