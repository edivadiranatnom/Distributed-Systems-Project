import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GUI extends Application {
//    private static String serverIp;

    @Override
    public void start(Stage stage) {
        GUIController GUIController = new GUIController();
        stage.setScene(new Scene(GUIController));
        stage.setTitle("Uno");
        stage.setWidth(1080);
        stage.setHeight(720);
        stage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
