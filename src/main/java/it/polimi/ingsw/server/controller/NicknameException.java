package it.polimi.ingsw.server.controller;

public class NicknameException extends Exception{

    public NicknameException(){
        System.err.println(toString()+"Nickname can't be taken, can not connect!");
    }

    public NicknameException(String player){
        System.err.println(toString()+player+" tried to join!");
    }

    @Override
    public String toString() {
        return "NicknameException:";
    }

}
