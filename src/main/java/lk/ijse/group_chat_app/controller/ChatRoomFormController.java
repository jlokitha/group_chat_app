package lk.ijse.group_chat_app.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import lk.ijse.group_chat_app.InputHandler;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ChatRoomFormController implements Runnable, Initializable {

    @FXML
    private VBox vbox;

    @FXML
    private TextField txtMessage;

    private Socket remoteSocket;

    private DataOutputStream out;

    private DataInputStream in;

    public static String username;

    InputHandler inputHandler;

    private ChatRoomFormController client;

    public ChatRoomFormController() {
        client = this;
    }

    @Override
    public void run () {
        try {
            remoteSocket = new Socket ( "192.168.1.108", 5000 );
            in = new DataInputStream ( new BufferedInputStream ( remoteSocket.getInputStream ( ) ) );
            out = new DataOutputStream ( remoteSocket.getOutputStream ( ) );

            inputHandler = new InputHandler ( out );

            inputHandler.handleInput ( "Username/" + username );

            String message;

            while ( ( message = in.readUTF ( ) ) != null ) {
                System.out.println (message );
                setTextToVbox ( message );
            }
        } catch ( IOException e ) {
            e.printStackTrace ();
        }
    }

    @FXML
    void btnGalleryOnAction( ActionEvent event) {

    }

    public void setTextToVbox(String message) {
        Platform.runLater(() -> {
            TextFlow text = new TextFlow(new Text(message));

            if (message.startsWith(username)) {
                text.setTextAlignment(TextAlignment.RIGHT);
            } else {
                text.setTextAlignment(TextAlignment.LEFT);
            }

            vbox.getChildren().add(text);
        });
    }

    @FXML
    void btnSendOnAction(ActionEvent event) {
        try {
            inputHandler.handleInput ( txtMessage.getText () );
        } catch ( IOException e ) {
            e.printStackTrace ();
        }
    }

    @Override
    public void initialize ( URL url, ResourceBundle resourceBundle ) {
        new Thread ( () -> {
            client.run ();
        } ).start ();
    }
}
