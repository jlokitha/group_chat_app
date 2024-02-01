package lk.ijse.group_chat_app.controller;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class GreenImagePaneFormController {

    @FXML
    public VBox vbox;

    public void setData ( Image image, String time ) {

        vbox.setAlignment ( Pos.TOP_RIGHT );

        ImageView imageView = new ImageView ( image );
        imageView.setFitWidth ( 300 );  //Set image view width to 300
        imageView.setFitHeight ( 300 );  //Set image view height to 300
        imageView.setPreserveRatio ( true );  //Maintains the original proportions of the image

        Label label = new Label ( time );
        label.setTextFill ( Color.rgb(114, 119, 116) );

        vbox.getChildren ().add ( imageView );
        vbox.getChildren ().add ( label );

    }

}
