package it.polimi.ingsw.controller;

public class GameIsFullException extends Exception{

    public GameIsFullException(){
        System.err.println(toString()+"Game capacity already full, can not connect!");
    }

    public GameIsFullException(String player){
        System.err.println(toString()+player+" tried to join!");
    }

    @Override
    public String toString() {
        return "GameIsFullException:";
    }

}
