package com.leejs1030.connectfour;

public class Cell{
    private char val;
    public Cell(){this.val = '.';};
    public Cell(char val){this.val = val;};
    public Cell(Cell c) { this.val = c.val; }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Cell) return this.val == ((Cell)obj).val;
	    else return this.val == ((Character)obj);
    }

    public String toString(){
        return String.valueOf(this.val);
    }

    public char set(char val){
        this.val = val;
        return this.val;
    }

    public char set(String val){
        this.val = val.charAt(0);
        return this.val;
    }

    public char get(){
        return this.val;
    }
}
