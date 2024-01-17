package lk.ijse.group_chat_app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class AppInitializer extends Application {

    public static void main ( String[] args ) {
        launch ( args );
    }

    @Override
    public void start ( Stage stage ) throws Exception {
        AnchorPane root = FXMLLoader.load ( getClass ( ).getResource ( "/view/loginForm.fxml" ) );
        stage.setScene ( new Scene ( root ) );
        stage.centerOnScreen ();
        stage.show ();
    }
}
