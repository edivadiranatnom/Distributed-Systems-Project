package UnoGame;
import java.io.Serializable;
import java.util.*;

public class Rules implements Serializable{
    public boolean passport(Game uno, Card cardToPlay){
        if((uno.currentColor.equals(cardToPlay.color)) || (uno.peekScarti().card == cardToPlay.card)) {
            return true;
        }
        return false;
    }
}
