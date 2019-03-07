package UnoGame;
import java.util.*;

public class Game {
    public Deck mazzo = new Deck();
    public Game(){}

    public void stampa () {
        for (int i = 0; i< mazzo.carddeck.length; i++) {
            System.out.println(mazzo.carddeck[i].card+" "+mazzo.carddeck[i].color+"\n");
        }
    }
    // potrei farla anche void
    public Deck shuffle(){
        Random rgen = new Random();

        for (int i=0; i<mazzo.carddeck.length; i++) {
            int randomPosition = rgen.nextInt(mazzo.carddeck.length);
            Card temp = mazzo.carddeck[i];
            mazzo.carddeck[i] = mazzo.carddeck[randomPosition];
            mazzo.carddeck[randomPosition] = temp;
        }

        return mazzo;
    }
}
