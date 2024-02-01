package lk.ijse.group_chat_app.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.regex.Pattern;

public class LoginFormController {
    @FXML
    public TextField txtUsername;

    @FXML
    public void btnLoginOnAction ( ActionEvent event ) {
        if ( validateUsername ( txtUsername.getText (  ) ) ) {
            ChatRoomFormController.username = txtUsername.getText ();
            try {
                Parent parent = FXMLLoader.load ( this.getClass ().getResource ( "/view/chatRoomForm.fxml" ) );
                Stage stage = (Stage) ((Node) event.getSource ()).getScene ().getWindow ();
                stage.setScene ( new Scene ( parent ) );
                stage.centerOnScreen ();
                stage.show ();
            } catch ( IOException e ) {
                e.printStackTrace ();
            }
        }
    }

    @FXML
    public void imgCloseOnMouseClicked ( MouseEvent mouseEvent ) {
        System.exit ( 0 );
    }

    @FXML
    public void imgMinimizeOnMouseClicked ( MouseEvent mouseEvent ) {
        Stage stage = (Stage)((ImageView)mouseEvent.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    public void txtUsernameOnAction ( ActionEvent actionEvent ) {
        btnLoginOnAction ( actionEvent );
    }

    public boolean validateUsername (String username) {
        return Pattern.matches ( "([a-zA-Z0-9]{3,})", username );
    }
}
