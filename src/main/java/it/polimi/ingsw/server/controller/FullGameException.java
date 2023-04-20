package it.polimi.ingsw.server.controller;

public class FullGameException extends Exception{

    public FullGameException(){
        System.err.println(toString()+"Game capacity already full, can not connect!");
    }

    public FullGameException(String player){
        System.err.println(toString()+player+" tried to join!");
    }

    @Override
    public String toString() {
        return "FullGameException:";
    }

}
