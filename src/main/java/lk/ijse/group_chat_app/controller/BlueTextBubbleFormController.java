package lk.ijse.group_chat_app.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class BlueTextBubbleFormController {
    @FXML
    public Label lblMessage;

    @FXML
    public Label lblTime;

    public void setData( String message, String time ) {
        if ( message != null ) {
            lblMessage.setText ( message );
            lblTime.setText ( time );
        }
    }
}
