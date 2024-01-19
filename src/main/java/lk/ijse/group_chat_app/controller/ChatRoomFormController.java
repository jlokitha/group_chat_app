package lk.ijse.group_chat_app.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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

    private BufferedReader in;

    public static String username;

    @FXML
    void btnGalleryOnAction( ActionEvent event) {

    }

    @FXML
    void btnSendOnAction(ActionEvent event) {
        try {
            out.writeUTF ( txtMessage.getText () );
            txtMessage.clear ();
        } catch ( IOException e ) {
            e.printStackTrace ();
        }
    }

    @Override
    public void run () {
        try {
//            out.writeUTF ("Username/" + username);
//            out.flush ();

            String message;

            while ( true ) {

                if ( (message = in.readLine()) != null ) {
                    TextFlow text = new TextFlow ( new Text ( message ) );

                    if ( message.startsWith ( username ) ) {
                        text.setTextAlignment ( TextAlignment.RIGHT );
                    } else {
                        text.setTextAlignment ( TextAlignment.LEFT );
                    }

                    vbox.getChildren ().add ( text );
                }
            }
        } catch ( IOException e ) {
            e.printStackTrace ();
        }
    }

    @Override
    public void initialize ( URL url, ResourceBundle resourceBundle ) {
        try {
            remoteSocket = new Socket ( "192.168.1.108", 5000 );
            in = new BufferedReader ( new InputStreamReader ( remoteSocket.getInputStream ( ) ) );
            out = new DataOutputStream ( remoteSocket.getOutputStream ( ) );
//            run ();
        } catch ( IOException e ) {
            e.printStackTrace ();
        }
    }
}
