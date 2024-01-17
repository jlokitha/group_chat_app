package lk.ijse.group_chat_app.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class LoginFormController {
    @FXML
    public TextField txtUserName;
    public static String userName;

    @FXML
    public void btnLoginOnAction ( ActionEvent event ) {
        userName = txtUserName.getText ();
        try {
            Parent parent = FXMLLoader.load ( Objects.requireNonNull ( LoginFormController.class.getResource ( "/view/chatRoomForm.fxml" ) ) );
            Stage stage = (Stage) ((Node) event.getSource ()).getScene ().getWindow ();
            stage.setScene ( new Scene ( parent ) );
            stage.centerOnScreen ();
            stage.show ();
        } catch ( IOException e ) {
            e.printStackTrace ();
        }
    }
}
