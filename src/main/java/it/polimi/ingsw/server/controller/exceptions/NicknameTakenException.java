package it.polimi.ingsw.server.controller.exceptions;

public class NicknameTakenException extends Exception{

    public NicknameTakenException(){
        System.err.println(this +"Nickname is already taken, can not connect!");
    }

    public NicknameTakenException(String player){
        System.err.println(this +player+" tried to join!");
    }

    @Override
    public String toString() {
        return "NicknameTakenException:";
    }

}
