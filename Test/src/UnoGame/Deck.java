package UnoGame;

import java.io.Serializable;
import java.util.*;

public class Deck implements Serializable {
    public ArrayList<Card> carddeck = new ArrayList<>();

    //Le carte vanno da 0 9. Il "salta turno" vale 10, il "cambio giro" vale 12, il "+2" vale 11, il "cambia colore" vale 13, il "+4" vale 14.
    // 13 e 14 non hanno colore.
    public int[] typeCard = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14};
    public String[] colorCard = new String[]{"green", "yellow", "red", "blue", "black"};

    public  Deck() {

        for(int i = 0; i<112; i++){
            carddeck.add(i, new Card());
        }

        int index = -1;
        // carte comuni
        for (int idx=0; idx<2; idx++){
            System.out.println("carte comuni: "+index);
            for (int i = 0; i < typeCard.length-2; i++) {
                for (int j = 0; j < colorCard.length-1; j++) {
                    index++;
                    //carddeck.iterator().next().color = colorCard[j];
                    carddeck.get(index).color = colorCard[j];
                    carddeck.get(index).card = typeCard[i];
                    carddeck.get(index).background = colorCard[j] +"_"+typeCard[i]+".png";
                    //get(index).add(typeCard[i], colorCard[j]);
                }
            }
        }

        // carte speciali
        for (int j = 0; j<4; j++) {
            System.out.println("carte speciali: "+index);
            for (int i = 13; i < 15; i++) {
                index++;
                //carddeck.iterator().next().color = colorCard[j];
                carddeck.get(index).color = "black";
                carddeck.get(index).card = i;
                carddeck.get(index).background = "black_"+i+".png";
                //get(index).add(typeCard[i], colorCard[j]);
            }
        }
    }

    public Card pop(){
        Card c = carddeck.get(carddeck.size() - 1);
        carddeck.remove(carddeck.size() - 1);
        return c;
    }

    public void push(Card c){
        carddeck.add(c);
    }

    public void shuffle(){
        Random rgen = new Random();

        for (int i=0; i<carddeck.size(); i++) {
            int randomPosition = rgen.nextInt(carddeck.size());
            Card temp = carddeck.get(i);
            carddeck.set(i, carddeck.get(randomPosition));
            carddeck.set(randomPosition, temp);
        }
    }
}