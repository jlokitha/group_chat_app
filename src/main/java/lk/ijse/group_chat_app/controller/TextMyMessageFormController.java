package lk.ijse.group_chat_app.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class TextMyMessageFormController {
    @FXML
    public Text txtMessage;

    public void setData( String message ) {
        if ( message != null ) {
            txtMessage.setText ( message );
        }
    }
}
