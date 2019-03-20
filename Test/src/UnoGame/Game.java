package UnoGame;

import java.io.Serializable;
import java.util.*;

public class Game implements Serializable {
    public Deck mazzo;
    public ArrayList<Card> MyCard = new ArrayList<>();
    public HashMap<String, Integer> NumberAllPlayersCards = new HashMap<>();
    private String leader;
    public Game(){
        mazzo = new Deck();
    }

    public void stampaCarte () {
        for (int i = 0; i < MyCard.size(); i++) {
            System.out.println(MyCard.get(i).card + MyCard.get(i).color);
        }
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }
    public String getLeader(){
        return this.leader;
    }
}