package it.polimi.ingsw.server.controller;

public class NicknameTakenException extends Exception{

    public NicknameTakenException(){
        System.err.println(toString()+"Nickname is already taken, can not connect!");
    }

    public NicknameTakenException(String player){
        System.err.println(toString()+player+" tried to join!");
    }

    @Override
    public String toString() {
        return "NicknameTakenException:";
    }

}
