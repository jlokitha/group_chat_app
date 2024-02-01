package lk.ijse.group_chat_app.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class WhiteTextBubbleFormController {
    @FXML
    public Label lblName;

    @FXML
    public Label lblMessage;

    @FXML
    public Label lblTime;

    public void setData ( String sender , String message , String time ) {
        lblName.setText ( sender );
        lblMessage.setText ( message );
        lblTime.setText ( time );

        lblTime.setLayoutX ( lblMessage.getText ().length () * 8.7 );  //Move label to right side
    }
}
