package lk.ijse.group_chat_app.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class GreyTextBubbleFormController {
    @FXML
    public Label lblName;

    @FXML
    public Label lblMessage;

    @FXML
    public Label lblTime;

    public void setData(String sender, String message, String time) {
        if (sender != null && message != null) {
            lblName.setText ( sender );
            lblMessage.setText ( message );
            lblTime.setText ( time );
        }
    }
}
