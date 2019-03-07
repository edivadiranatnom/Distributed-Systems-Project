package UnoGame;

import java.io.Serializable;

public class Deck implements Serializable {
    public Card[] carddeck = new Card[52];

    //Le carte vanno da 0 9. Il "salta turno" vale 10, il "cambio giro" vale 11, il "+2" vale 12, il "cambia colore" vale 13, il "+4" vale 14.
    // 13 e 14 non hanno colore.
    public int[] typeCard = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
    public String[] colorCard = new String[]{"verde", "giallo", "rosso", "blu"};

    public  Deck() {
        for (int i = 0; i<52; i++) {
            carddeck[i] = new Card();
        }
        int index = -1;
        for (int i = 0; i < typeCard.length; i++) {
            for (int j = 0; j < colorCard.length; j++) {
                index++;
                carddeck[index].card = typeCard[i];
                carddeck[index].color = colorCard[j];
            }
        }
    }
}
