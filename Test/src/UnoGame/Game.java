package UnoGame;
import java.io.Serializable;
import java.util.*;

public class Game implements Serializable {
    public Deck mazzo = new Deck();
    public HashMap<String, ArrayList<Card>> AllPlayersCards = new HashMap<>();
    public Game(){}

    public void stampa (String ip) {
        System.out.println("sei nella stampa: \n");
        ArrayList<Card> array = AllPlayersCards.get(ip);
        for (int i = 0; i < array.size(); i++) {
            System.out.println(array.get(i).card + ", " +array.get(i).color);
        }
    }
    // potrei farla anche void
    public Deck shuffle(){
        Random rgen = new Random();

        for (int i=0; i<mazzo.carddeck.size(); i++) {
            int randomPosition = rgen.nextInt(mazzo.carddeck.size());
            Card temp = mazzo.carddeck.get(i);
            mazzo.carddeck.set(i, mazzo.carddeck.get(randomPosition));
            mazzo.carddeck.set(randomPosition, temp);
        }

        return mazzo;
    }
}
