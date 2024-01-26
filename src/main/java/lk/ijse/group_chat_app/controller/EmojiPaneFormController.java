package lk.ijse.group_chat_app.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class EmojiPaneFormController implements Initializable {

    @FXML
    private Label lbl1;

    @FXML
    private Label lbl2;

    @FXML
    private Label lbl3;

    @FXML
    private Label lbl4;

    @FXML
    private Label lbl5;

    @FXML
    private Label lbl6;

    @FXML
    private Label lbl7;

    @FXML
    private Label lbl8;

    @FXML
    private Label lbl9;

    @FXML
    private Label lbl10;

    @FXML
    private Label lbl11;

    @FXML
    private Label lbl12;

    @FXML
    private Label lbl13;

    @FXML
    private Label lbl14;

    @FXML
    private Label lbl15;

    @FXML
    private Label lbl16;

    @FXML
    private Label lbl17;

    @FXML
    private Label lbl18;

    @FXML
    private Label lbl19;

    @FXML
    private Label lbl20;

    private ChatRoomFormController controller;

    public void setController (ChatRoomFormController controller) {
        this.controller = controller;
    }

    @FXML
    void lbl1OnMouseClicked(javafx.scene.input.MouseEvent event) {
        controller.setEmojiToTxt ( "\uD83D\uDE00" );
    }

    @FXML
    void lbl2OnMouseClicked(javafx.scene.input.MouseEvent event) {
        controller.setEmojiToTxt ( "\uD83D\uDE04" );
    }

    @FXML
    void lbl3OnMouseClicked(javafx.scene.input.MouseEvent event) {
        controller.setEmojiToTxt ( "\uD83D\uDE02" );
    }

    @FXML
    void lbl4OnMouseClicked(javafx.scene.input.MouseEvent event) {
        controller.setEmojiToTxt ( "\uD83D\uDE0A" );
    }

    @FXML
    void lbl5OnMouseClicked(javafx.scene.input.MouseEvent event) {
        controller.setEmojiToTxt ( "\uD83D\uDE07" );
    }

    @FXML
    void lbl6OnMouseClicked(javafx.scene.input.MouseEvent event) {
        controller.setEmojiToTxt ( "\uD83D\uDE0D" );
    }

    @FXML
    void lbl7OnMouseClicked( javafx.scene.input.MouseEvent event) {
        controller.setEmojiToTxt ( "\uD83D\uDE10" );
    }

    @FXML
    void lbl8OnMouseClicked(javafx.scene.input.MouseEvent event) {
        controller.setEmojiToTxt ( "\uD83D\uDE0F" );
    }

    @FXML
    void lbl9OnMouseClicked(javafx.scene.input.MouseEvent event) {
        controller.setEmojiToTxt ( "\uD83D\uDE12" );
    }

    @FXML
    void lbl10OnMouseClicked(javafx.scene.input.MouseEvent event) {
        controller.setEmojiToTxt ( "\uD83D\uDE0C" );
    }

    @FXML
    void lbl11OnMouseClicked(javafx.scene.input.MouseEvent event) {
        controller.setEmojiToTxt ( "\uD83D\uDE0E" );
    }

    @FXML
    void lbl12OnMouseClicked(javafx.scene.input.MouseEvent event) {
        controller.setEmojiToTxt ( "\uD83D\uDE2D" );
    }

    @FXML
    void lbl13OnMouseClicked(javafx.scene.input.MouseEvent event) {
        controller.setEmojiToTxt ( "\uD83D\uDE31" );
    }

    @FXML
    void lbl14OnMouseClicked(javafx.scene.input.MouseEvent event) {
        controller.setEmojiToTxt ( "\uD83D\uDE21" );
    }

    @FXML
    void lbl15OnMouseClicked(javafx.scene.input.MouseEvent event) {
        controller.setEmojiToTxt ( "\uD83D\uDC4B" );
    }

    @FXML
    void lbl16OnMouseClicked(javafx.scene.input.MouseEvent event) {
        controller.setEmojiToTxt ( "\uD83D\uDC4D" );
    }

    @FXML
    void lbl17OnMouseClicked(javafx.scene.input.MouseEvent event) {
        controller.setEmojiToTxt ( "\uD83D\uDC4E" );
    }

    @FXML
    void lbl18OnMouseClicked(javafx.scene.input.MouseEvent event) {
        controller.setEmojiToTxt ( "\uD83D\uDC4F" );
    }

    @FXML
    void lbl19OnMouseClicked(javafx.scene.input.MouseEvent event) {
        controller.setEmojiToTxt ( "\uD83D\uDE4F" );
    }


    @FXML
    void lbl20OnMouseClicked(javafx.scene.input.MouseEvent event) {
        controller.setEmojiToTxt ( "\uD83D\uDC40" );
    }

    @Override
    public void initialize ( URL url, ResourceBundle resourceBundle ) {
        lbl1.setText ( "\uD83D\uDE00" );
        lbl2.setText ( "\uD83D\uDE04" );
        lbl3.setText ( "\uD83D\uDE02" );
        lbl4.setText ( "\uD83D\uDE0A" );
        lbl5.setText ( "\uD83D\uDE07" );
        lbl6.setText ( "\uD83D\uDE0D" );
        lbl7.setText ( "\uD83D\uDE10" );
        lbl8.setText ( "\uD83D\uDE0F" );
        lbl9.setText ( "\uD83D\uDE12" );
        lbl10.setText ( "\uD83D\uDE0C" );
        lbl11.setText ( "\uD83D\uDE0E" );
        lbl12.setText ( "\uD83D\uDE2D" );
        lbl13.setText ( "\uD83D\uDE31" );
        lbl14.setText ( "\uD83D\uDE21" );
        lbl15.setText ( "\uD83D\uDC4B" );
        lbl16.setText ( "\uD83D\uDC4D" );
        lbl17.setText ( "\uD83D\uDC4E" );
        lbl18.setText ( "\uD83D\uDC4F" );
        lbl19.setText ( "\uD83D\uDE4F" );
        lbl20.setText ( "\uD83D\uDC40" );
    }
}
