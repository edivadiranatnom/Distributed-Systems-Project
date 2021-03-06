package UnoGame;

import java.io.Serializable;
import java.util.*;

public class Game implements Serializable {
    public Deck mazzo;
    private Stack<Card> scarti = new Stack<>();
    public String giocatoreTurno;
    public boolean isMyTurn = false;
    public boolean giroOrario = true;
    public boolean pescato = false;
    public String currentColor;
    public int numAllDeath = 0;
    public ArrayList<Card> MyCard = new ArrayList<>();
    public HashMap<String, ArrayList<String>> NumberAllPlayersCards = new HashMap<>();
    private String leader;

    public Game() {
        mazzo = new Deck();
    }

    public void stampaCarte() {
        for (int i = 0; i < MyCard.size(); i++) {
            System.out.println(MyCard.get(i).card + " " + MyCard.get(i).color);
        }
    }

    public void setLeader(String leader, int port) {
        this.leader = leader + ":" + port;
    }

    public String getLeader() {
        return this.leader;
    }

    public Card peekScarti() {
        return scarti.peek();
    }

    public Card popScarti() {
        return scarti.pop();
    }

    public void pushScarti(Card card) {
        scarti.push(card);
    }
}