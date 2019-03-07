package UnoGame;

import java.util.*;

public class UnoGame {
    public static Deck mazzo;
    public UnoGame() {
        mazzo = new Deck();
    }
    public void stampa () {
        System.out.println("lunghezza del deck"+mazzo.carddeck.length);
        for (int i = 0; i < mazzo.carddeck.length; i++) {
            System.out.println(""+mazzo.carddeck[i].card+", "+mazzo.carddeck[i].color);
        }
    }
    public static Card[] shuffle(){
        Random rgen = new Random();

        for (int i=0; i<mazzo.carddeck.length; i++) {
            int randomPosition = rgen.nextInt(mazzo.carddeck.length);
            Card temp = mazzo.carddeck[i];
            mazzo.carddeck[i] = mazzo.carddeck[randomPosition];
            mazzo.carddeck[randomPosition] = temp;
        }

        return mazzo.carddeck;
    }
}
