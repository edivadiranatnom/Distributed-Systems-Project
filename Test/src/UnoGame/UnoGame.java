package UnoGame;

public class UnoGame {
    public static Deck mazzo = new Deck();
    public UnoGame() {
    }
    public void stampa () {
        System.out.println("lunghezza del deck"+mazzo.carddeck.length);
        for (int i = 0; i < mazzo.carddeck.length; i++) {
            System.out.println(""+mazzo.carddeck[i].card+", "+mazzo.carddeck[i].color);
        }
    }
}
