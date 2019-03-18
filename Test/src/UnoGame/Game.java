package UnoGame;

import java.io.Serializable;
import java.util.*;

public class Game implements Serializable {
    public Deck mazzo = new Deck();
    public HashMap<String, ArrayList<Card>> AllPlayersCards = new HashMap<>();
    public Game(){}

    public void stampaCarte (String ip) {

        ArrayList<Card> array = AllPlayersCards.get(ip);
        System.out.println("Player ip:" + ip +":");
        for (int i = 0; i < array.size(); i++) {
            System.out.println(array.get(i).card + array.get(i).color);
        }
    }
}