package com.leejs1030.connectfour.myexception;

interface WrongInputType { int OUT_OF_RANGE = 0, ALREADY_FULL = 1; }

public class WrongInputException extends Exception{
    int type;
    public WrongInputException(int type){
        super("해당 위치에는 둘 수 없습니다!");
        this.type = type;
    }

    public String getMessage(){
        switch(this.type){
        case WrongInputType.OUT_OF_RANGE:
            return "1 이상 7 이하의 자연수로 입력해 주세요.";
        case WrongInputType.ALREADY_FULL:
            return "더 이상 놓을 자리가 없는 곳입니다!";
        default:
            return "알 수 없는 오류입니다.";
        }
    }
}