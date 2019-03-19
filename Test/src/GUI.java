import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GUI extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader startLoader = new FXMLLoader(getClass().getResource("Start.fxml"));
        GUIController GUIController = new GUIController();

        startLoader.setController(GUIController);
        Parent startRoot = startLoader.load();

        stage.setScene(new Scene(startRoot));
        stage.setTitle("Uno");
        stage.setWidth(1080);
        stage.setHeight(720);
        stage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}