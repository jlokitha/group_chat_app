package lk.ijse.group_chat_app.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lk.ijse.group_chat_app.OutputHandler;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
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

    OutputHandler outputHandler;

    private ChatRoomFormController client;

    public ChatRoomFormController() {
        client = this;
    }

    @Override
    public void run () {
        try {
            remoteSocket = new Socket ( "192.168.1.109", 5000 );
            in = new DataInputStream ( new BufferedInputStream ( remoteSocket.getInputStream ( ) ) );
            out = new DataOutputStream ( remoteSocket.getOutputStream ( ) );

            outputHandler = new OutputHandler ( out );

            outputHandler.handleTextOutput ( "Username " + username );

            String message;

            while ( ( message = in.readUTF ( ) ) != null ) {

                if ( message.equals ( "/txt" ) ) {

                    setTextToVbox ( in.readUTF () );

                } else if ( message.equals ( "/img" ) ) {

                    int imageLength = in.readInt ( );

                    String sender = in.readUTF ();

                    byte[] imageData = new byte[imageLength];
                    in.readFully ( imageData );

                    setImgToVbox ( sender, imageData );
                }

            }
        } catch ( IOException e ) {
            e.printStackTrace ();
        }
    }

    @FXML
    void btnGalleryOnAction( ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image File");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File file = fileChooser.showOpenDialog ( txtMessage.getScene ( ).getWindow ( ) );

        if (file != null) {

            byte[] imageBytes = Files.readAllBytes(file.toPath());

            outputHandler.handleImageOutput ( imageBytes );
        }
    }

    public void setTextToVbox(String message) {
        Platform.runLater ( ( ) -> {
            TextFlow text = new TextFlow ( new Text ( message ) );

            if ( message.startsWith ( username ) ) {
                text.setTextAlignment ( TextAlignment.RIGHT );
            } else {
                text.setTextAlignment ( TextAlignment.LEFT );
            }

            vbox.getChildren ( ).add ( text );

        } );
    }

    public void setImgToVbox( String sender, byte[] imageData ) {
        Platform.runLater(() -> {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(imageData);
            Image image = new Image(inputStream);

            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(400);
            imageView.setFitHeight(400);
            imageView.setPreserveRatio(true);

            if ( !sender.startsWith ( username ) ) {

                TextFlow text = new TextFlow ( new Text ( sender ) );
                text.setTextAlignment ( TextAlignment.LEFT );
                vbox.getChildren ().add ( text );

                vbox.setAlignment ( Pos.TOP_LEFT );
                imageView.setX ( 0 );
                imageView.setY ( 0 );
                vbox.getChildren ( ).add ( imageView );

            } else {

                TextFlow text = new TextFlow ( new Text ( "Me" ) );
                text.setTextAlignment ( TextAlignment.RIGHT );
                vbox.getChildren ().add ( text );

                vbox.setAlignment ( Pos.TOP_RIGHT );
                vbox.getChildren ().add ( imageView );
            }
        });
    }

    @FXML
    void btnSendOnAction(ActionEvent event) {
        try {
            outputHandler.handleTextOutput ( txtMessage.getText () );
            txtMessage.clear ();
        } catch ( IOException e ) {
            e.printStackTrace ();
        }
    }

    @FXML
    public void imgCloseOnMouseClicked ( MouseEvent mouseEvent ) {
        shutdown ();
        System.exit ( 0 );
    }

    @FXML
    public void imgMinimizeOnMouseClicked ( MouseEvent mouseEvent ) {
        Stage stage = ( Stage) ((ImageView)mouseEvent.getSource ()).getScene ().getWindow ();
        stage.setIconified ( true );
    }

    public void shutdown() {
        try {
            outputHandler.handleTextOutput ( "Shutdown" );
            in.close ();
            out.close ();

            if ( !remoteSocket.isClosed () ) {
                remoteSocket.close ();
            }
        } catch ( IOException e ) {
            // Ignore
        }
    }

    @Override
    public void initialize ( URL url, ResourceBundle resourceBundle ) {
        new Thread ( () -> {
            client.run ();
        } ).start ();
    }
}
