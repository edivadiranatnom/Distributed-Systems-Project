package UnoGame;

public class UnoGame {
    public static Deck mazzo = new Deck();
    public UnoGame() {
    }
    public void stampa () {
        System.out.println("lunghezza del deck"+mazzo.carddeck.length);
    }
}
