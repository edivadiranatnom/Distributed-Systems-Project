import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.*;
import UnoGame.*;

public class Player {
    public static Utility utility = new Utility();
    public ClientFunctions Client;
    PlayerInterface stubPlayer;
    public int portRegistry = 1099;
    public static ArrayList<String> listIpPlayer = new ArrayList<>();

    public Player() {
    }

    public static void main(String[] args) {
    }
    private void setRegistry() throws Exception {
        System.out.println("into setRegistry");
        try {
            LocateRegistry.createRegistry(portRegistry);
        }  catch (RemoteException e) {
            portRegistry = portRegistry + 1;
            System.out.println("rmiregistry already started. Try to set on port: " + portRegistry);
            setRegistry();
        }
    }

    Game startPlayer(String serverIp, GUIController gc) throws Exception {
        // start Gui
        String ip = utility.findIp();
        System.out.println("il mio ip: " + ip);
        String host = serverIp;
        setRegistry();
        System.out.println("la mia porta è: "+portRegistry);
        ip = ip + ":" + portRegistry;
        // server service
        try {
            Client = new ClientFunctions();
            Naming.rebind("rmi://" + ip + "/ciao", Client);

            System.err.println("Ready for Ips...");
        } catch (Exception e) {
            System.err.println("Player exception: " + e.toString());
            e.printStackTrace();
        }

        // client service
        String responseIpLeader = null;
        try {
            ConnectionInterface stub = (ConnectionInterface) Naming.lookup("rmi://" + host + ":1099" +"/connection");
            responseIpLeader = stub.connect(ip);
            listIpPlayer.clear();
            listIpPlayer = Client.listIpPlayer;
            Client.mioController = gc;
        } catch (Exception e) {
            System.err.println("Player exception: " + e.toString());
            e.printStackTrace();
        }
        Game uno = new Game();
        uno.setLeader(responseIpLeader, portRegistry);
        return uno;
    }

    void killServerFor(String serverIp) throws Exception {
        ConnectionInterface stub = (ConnectionInterface) Naming.lookup("rmi://" + serverIp + "/connection");
        try {
            stub.kill();
        } catch (Exception e) {
        }
    }

    /**
     * entri solo se sei il leader
     * inutile controllare con il mio ip
     * poppa() localmente dal mazzo 7 carte alla volta, per ogni Client
     * lookup su ogni Client: cardDistribution passando le sue 7 carte.
     * Finito il giro ne parte un altro dove comunica a tutti:
     *  - il turno
     *  - il mazzo
     *  - lo scarto
     */

    Game distribute(Game uno) throws Exception {
        String leader = uno.getLeader();
        uno.mazzo.shuffle();
        int nPlayers = listIpPlayer.size();
        int myIndex = listIpPlayer.indexOf(leader);
        for (int i = myIndex + 1; i < nPlayers + myIndex + 1; i++) {
            ArrayList<Card> playersCards = new ArrayList<>();
            for (int j = 0; j < 7; j++) {
                playersCards.add(uno.mazzo.pop());
            }
            stubPlayer = (PlayerInterface) Naming.lookup("rmi://" + listIpPlayer.get(i % nPlayers) + "/ciao");
            stubPlayer.cardDistribution(playersCards);

            ArrayList<String> tuple = new ArrayList<>();
            tuple.add("7");
            tuple.add(i % nPlayers + ".png");
            uno.NumberAllPlayersCards.put(listIpPlayer.get(i), tuple);
        }
        uno.pushScarti(uno.mazzo.pop());
        uno.currentColor = uno.peekScarti().color;
        uno.giocatoreTurno = listIpPlayer.get((myIndex+1)%nPlayers);

        for (int i = myIndex + 1; i < nPlayers + myIndex + 1; i++) {
            stubPlayer = (PlayerInterface) Naming.lookup("rmi://" + listIpPlayer.get(i % nPlayers) + "/ciao");
            stubPlayer.preStartGame(uno);
        }
        return uno;
    }

    void communicateCardPlayed(Game uno, Card cartagiocata, Boolean skip) throws Exception{
        int nPlayers = listIpPlayer.size();
        int myIndex = listIpPlayer.indexOf(uno.giocatoreTurno);
        System.out.println("Sono in p.c. E' il mo turno, cambio: "+listIpPlayer.indexOf(uno.giocatoreTurno)+". Lo scarto è: "+uno.peekScarti().card +", "+uno.peekScarti().color);
        if (uno.giroOrario) {
            if(!skip) {
                if (uno.peekScarti().card == 10) {
                    // Inverto il turno nello stato globale?
                    System.out.println("Carta particolare. Si inverte il turno");
                    uno.giroOrario = false;
                    if (myIndex == 0) {
                        System.out.println("Fine di merda, sono lo 0. Setto a mano l'ultimo");
                        uno.giocatoreTurno = listIpPlayer.get(listIpPlayer.size() - 1);
                    } else {
                        uno.giocatoreTurno = listIpPlayer.get((myIndex - 1) % nPlayers);
                    }
                } else if (uno.peekScarti().card == 12) {
                    System.out.println("Carta particolare. Salta un player");
                    uno.giocatoreTurno = listIpPlayer.get((myIndex + 2) % nPlayers);
                } else {
                    uno.giocatoreTurno = listIpPlayer.get((myIndex + 1) % nPlayers);
                }
                System.out.println("Sono in p.c. ora il turno è di: " + listIpPlayer.indexOf(uno.giocatoreTurno));
                for (int i = myIndex + 1; i < nPlayers + myIndex; i++) {
                    stubPlayer = (PlayerInterface) Naming.lookup("rmi://" + listIpPlayer.get(i % nPlayers) + "/ciao");
                    System.out.println("giroOrario: " + uno.giroOrario);
                    stubPlayer.communicationCard(uno, cartagiocata);
                }
            }else{
                System.out.println("\nSono dentro skip branch\n");
                //Ho pescato una carta dal mazzo
                uno.giocatoreTurno = listIpPlayer.get((myIndex + 1) % nPlayers);
                for (int i = myIndex + 1; i < nPlayers + myIndex; i++) {
                    stubPlayer = (PlayerInterface) Naming.lookup("rmi://" + listIpPlayer.get(i % nPlayers) + "/ciao");
                    System.out.println("giroOrario: " + uno.giroOrario);
                    stubPlayer.removeDrawedCard(cartagiocata, uno.giocatoreTurno);
                }
            }
        } else {
            if (uno.peekScarti().card == 10) {
                // Inverto il turno nello stato globale?
                System.out.println("Carta particolare. Si inverte il turno");
                uno.giroOrario = true;
                uno.giocatoreTurno = listIpPlayer.get((myIndex+1)%nPlayers);
            } else if (uno.peekScarti().card == 12) {
                System.out.println("Carta particolare. Salta un player");
                if(myIndex == 1){
                    System.out.println("Fine di merda, sono l'1. Setto a mano l'ultimo");
                    uno.giocatoreTurno = listIpPlayer.get(listIpPlayer.size()-1);
                }
                else {
                    uno.giocatoreTurno = listIpPlayer.get((myIndex-2)%nPlayers);
                }
            } else {
                if(myIndex == 0){
                    System.out.println("Fine di merda, sono lo 0. Setto a mano l'ultimo");
                    uno.giocatoreTurno = listIpPlayer.get(listIpPlayer.size()-1);
                }
                else {
                    uno.giocatoreTurno = listIpPlayer.get((myIndex-1)%nPlayers);
                }            }
            System.out.println("Sono in p.c. ora il turno è di: "+listIpPlayer.indexOf(uno.giocatoreTurno));
            for (int i = myIndex + 1; i < nPlayers + myIndex; i++) {
                stubPlayer = (PlayerInterface) Naming.lookup("rmi://" + listIpPlayer.get(i % nPlayers) + "/ciao");
                stubPlayer.communicationCard(uno, cartagiocata);
            }
        }
    }
}
