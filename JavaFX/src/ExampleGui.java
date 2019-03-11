import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ExampleGui extends Application {
    @Override
    public void start ( Stage stage ) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource ("./ExampleGui.fxml"));

        Scene scene = new Scene(root,1200 ,800);
        stage.setTitle ("JavaFX Complete Example ") ;

        stage.setScene(scene);

        stage.show();
    }
    public static void main (String [] args) {
        launch(args) ;
    }
}