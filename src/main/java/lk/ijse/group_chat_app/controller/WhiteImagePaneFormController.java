package lk.ijse.group_chat_app.controller;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class WhiteImagePaneFormController {

    @FXML
    public VBox vbox;

    public void setData ( String sender, Image image, String time ) {
        Label lblSender = new Label ( sender );
        lblSender.setTextFill ( Color.rgb (44, 44, 212) );
        lblSender.setFont(new Font (14));

        ImageView imageView = new ImageView ( image );
        imageView.setFitWidth ( 300 );
        imageView.setFitHeight ( 300 );
        imageView.setPreserveRatio ( true );

        Label lblTime = new Label ( time );
        lblTime.setTextFill ( Color.rgb(114, 119, 116) );

        vbox.getChildren ().add ( lblSender );
        vbox.getChildren ().add ( imageView );
        vbox.getChildren ().add ( lblTime );
        VBox.setMargin(lblTime, new Insets (0, 0, 0, 262));
    }

}
