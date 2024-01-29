package lk.ijse.group_chat_app.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class TextOtherMessageFormController {
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
        }
    }
}
