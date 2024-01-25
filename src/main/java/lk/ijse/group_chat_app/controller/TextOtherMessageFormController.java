package lk.ijse.group_chat_app.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class TextOtherMessageFormController {
    @FXML
    public Label lblName;

    @FXML
    public Text txtMessage;

    public void setData(String sender, String message) {
        if (sender != null && message != null) {
            lblName.setText ( sender );
            txtMessage.setText ( message );
        }
    }
}
