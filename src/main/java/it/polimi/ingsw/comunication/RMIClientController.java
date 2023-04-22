package it.polimi.ingsw.comunication;

public class RMIClientController implements ClientController {
    private RMIClient rmiClient;
    @Override
    public void printPoints() {
        rmiClient.printPoints();
    }
}
