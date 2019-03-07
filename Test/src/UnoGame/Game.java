package UnoGame;
import java.io.Serializable;
import java.util.*;

public class Game implements Serializable {
    public Deck mazzo = new Deck();
    public Game(){}

    public void stampa (Deck deck) {
        System.out.println("sei nella stampa: \n");
        for (int i = 0; i< deck.carddeck.size(); i++) {
            System.out.println(deck.carddeck.get(i).card+" "+deck.carddeck.get(i).color+"\n");
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
