package com.leejs1030.connectfour;

import org.junit.Test;

public class WrongInputExceptionTest {
    @Test
    public void printErrorTest(){
        try{
            throw new WrongInputException(0);
        } catch(WrongInputException err){
            System.out.println("start");
            System.out.println(err);
            // System.out.println(err.getMessage());
            System.out.println(err.toString());
            System.out.println(err.getStackTrace());
            System.out.println("end");
        }
    }
}
