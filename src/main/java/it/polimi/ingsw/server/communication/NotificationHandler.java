package it.polimi.ingsw.server.communication;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class NotificationHandler {
    /**
     *
     * @param client client to witch respond
     * @param json json string that client sent
     * @return true if client still connected
     */
    public static boolean respond(Socket client, String json){
       switch (json){
           //TODO: non va fatto cosi
           case "disconnect":
               return false;
           case "ok":
               return true;
       }
       return true;
   }
}
