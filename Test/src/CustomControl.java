
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import UnoGame.*;
/**
 * Sample custom control hosting a text field and a button.
 */
public class CustomControl extends VBox {
    @FXML private TextField textField;
    public HashMap<String, ArrayList<Card>> CopiaCard = new HashMap<>();


    public CustomControl() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("custom_control.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        
        try {
            fxmlLoader.load();            
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
    
    public String getText() {
        if(textField == null){
            System.out.println("E' null");
        }
        System.out.println("stampa sta merda: "+ textField.textProperty().get());
        return textProperty().get();
    }
    
    public void setText(String value) {
        textProperty().set(value);
    }
    
    public StringProperty textProperty() {
        return textField.textProperty();                
    }
        
    @FXML
    protected void doSomething() {
        Player player = new Player();
        int ris = -1;
        try {
            ris = player.startPlayer("172.16.121.128");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
