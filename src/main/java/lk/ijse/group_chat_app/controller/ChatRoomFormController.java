package lk.ijse.group_chat_app.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ChatRoomFormController implements Runnable, Initializable {
    @FXML
    public AnchorPane emojiPane;

    @FXML
    public ScrollPane scrollPane;
    
    @FXML
    public Label lblUser;
    
    @FXML
    public Label lblOnlineCount;

    @FXML
    public ImageView imgSend;

    @FXML
    public ImageView imgEmoji;

    @FXML
    public ImageView imgAttatch;

    @FXML
    private VBox vbox;

    @FXML
    private TextField txtMessage;

    private Socket remoteSocket;

    private DataOutputStream out;

    private DataInputStream in;

    public static String username;

    private ChatRoomFormController client;

    private boolean emojiPaneVisible;

    public ChatRoomFormController() {
        client = this;
        emojiPaneVisible = false;
    }

    @Override
    public void run () {
        try {
            remoteSocket = new Socket ( "192.168.43.20", 5000 );
            in = new DataInputStream ( new BufferedInputStream ( remoteSocket.getInputStream ( ) ) );
            out = new DataOutputStream ( remoteSocket.getOutputStream ( ) );

            handleTextOutput ( ("Username " + username), "" );

            String message;

            while ( ( message = in.readUTF ( ) ) != null ) {

                 if ( message.equals ( "/info" ) ) {

                     String sender = in.readUTF ();
                     sender = (sender.equals ( username )) ? "you" : sender;
                     String msg = in.readUTF ();

                     setInfoToVbox ( sender, msg );

                 } else if ( message.equals ( "/txt" ) ) {

                    String sender = in.readUTF ();
                    String msg = in.readUTF ();
                    String time = in.readUTF ();

                    setTextToVbox ( sender, msg, time );

                } else if ( message.equals ( "/img" ) ) {

                    String sender = in.readUTF ();

                    int imageLength = in.readInt ( );

                    byte[] imageData = new byte[imageLength];
                    in.readFully ( imageData );

                    String time = in.readUTF ();

                    setImgToVbox ( sender, imageData, time );
                }

            }
        } catch ( IOException e ) {
            shutdown ();
        }
    }

    public void setInfoToVbox( String sender, String message) {
        Platform.runLater ( ( ) -> {

            TextFlow textFlow = new TextFlow ( new Text ( sender + " " + message ) );
            HBox hBox = new HBox ( );
            hBox.getChildren ().add ( textFlow );
            hBox.setAlignment ( Pos.CENTER );
            hBox.setPrefWidth ( 652 );
            vbox.getChildren ().add ( hBox );

        } );
    }

    public void setTextToVbox(String sender, String message, String time) {
        Platform.runLater ( ( ) -> {

            if ( !sender.equals ( username ) ) {
                setOtherMessage ( sender, message, time );
            } else {
                setMyMessage ( message, time );
            }

        } );
    }

    public void setOtherMessage ( String sender, String message, String time ) {
        try {
            FXMLLoader loader = new FXMLLoader ( ChatRoomFormController.class.getResource ( "/view/greyTextBubbleForm.fxml" ) );
            Parent root = loader.load ( );
            GreyTextBubbleFormController controller = loader.getController ( );
            controller.setData ( sender, message, time );
            vbox.getChildren ( ).add ( root );
            autoScrollDown ();
        } catch ( IOException e ) {
            e.printStackTrace ();
        }
    }

    public void setMyMessage (String message, String time) {
        try {
            FXMLLoader loader = new FXMLLoader ( ChatRoomFormController.class.getResource ( "/view/blueTextBubbleForm.fxml" ) );
            Parent root = loader.load ( );
            BlueTextBubbleFormController controller = loader.getController ( );
            controller.setData ( message, time );
            vbox.getChildren ( ).add ( root );
            autoScrollDown ();
        } catch ( IOException e ) {
            e.printStackTrace ();
        }
    }

    public void setImgToVbox( String sender, byte[] imageData, String time ) {
        Platform.runLater(() -> {

            try {
                if ( !sender.equals ( username ) ) {

                    FXMLLoader loader = new FXMLLoader ( ChatRoomFormController.class.getResource ( "/view/greyImagePaneForm.fxml" ) );
                    Parent root = loader.load ( );
                    GreyImagePaneFormController controller = loader.getController ( );
                    controller.setData ( sender, new Image ( new ByteArrayInputStream ( imageData ) ), time );
                    vbox.getChildren ( ).add ( root );
                    autoScrollDown ( );

                } else {

                    FXMLLoader loader = new FXMLLoader ( ChatRoomFormController.class.getResource ( "/view/blueImagePaneForm.fxml" ) );
                    Parent root = loader.load ( );
                    BlueImagePaneFormController controller = loader.getController ( );
                    controller.setData ( new Image ( new ByteArrayInputStream ( imageData ) ), time );
                    vbox.getChildren ( ).add ( root );
                    autoScrollDown ( );
                }
            } catch ( IOException e ) {
                e.printStackTrace ();
            }

        });
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
            handleTextOutput ( "Shutdown", "" );
            in.close ();
            out.close ();

            if ( !remoteSocket.isClosed () ) {
                remoteSocket.close ();
            }
        } catch ( IOException e ) {
            // Ignore
        }
    }

    public void setEmojiToTxt (String unicode) {
        txtMessage.appendText ( unicode );
    }

    public void loadEmojiPane () {
        try {
            emojiPane.getChildren().clear();
            FXMLLoader loader = new FXMLLoader ( ChatRoomFormController.class.getResource ( "/view/emojiPaneForm.fxml" ) );
            Parent root = loader.load ( );
            EmojiPaneFormController controller = loader.getController ( );
            controller.setController ( client );
            emojiPane.getChildren ().add ( root );
        } catch ( IOException e ) {
            e.printStackTrace ();
        }
    }

    public void handleTextOutput ( String message, String time ) {
        try {
            if ( ! message.startsWith ( "Username" ) && ! message.startsWith ( "Shutdown" ) ) {


                out.writeUTF ( "/txt" );
                out.flush ( );

                out.writeUTF ( message );
                out.flush ( );

                out.writeUTF ( time );
                out.flush ( );


            } else {

                out.writeUTF ( "/info" );
                out.flush ( );

                out.writeUTF ( message );
                out.flush ( );

            }
        } catch ( IOException e ) {
            e.printStackTrace ( );
        }
    }

    public void handleImageOutput ( byte[] imageBytes, String time) {
        try {
            out.writeUTF("/img");
            out.flush();

            out.writeInt(imageBytes.length);
            out.flush ();

            out.write(imageBytes);
            out.flush();

            out.writeUTF ( time );
            out.flush ();
        } catch ( IOException e ) {
            e.printStackTrace ();
        }
    }

    public void autoScrollDown () {
        scrollPane.needsLayoutProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                scrollPane.setVvalue(1.0);
            }
        });
    }

    @FXML
    void btnAttachOnAction(ActionEvent event) {
        try {
            FileChooser fileChooser = new FileChooser ( );
            fileChooser.setTitle ( "Choose Image File" );

            fileChooser.getExtensionFilters ( ).addAll (
                    new FileChooser.ExtensionFilter ( "Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif" )
            );

            File file = fileChooser.showOpenDialog ( txtMessage.getScene ( ).getWindow ( ) );

            if ( file != null ) {

                byte[] imageBytes = Files.readAllBytes ( file.toPath ( ) );

                handleImageOutput ( imageBytes, LocalTime.now ( ).format ( DateTimeFormatter.ofPattern ( "HH:mm" ) ) );
            }
        } catch ( IOException e ) {
            e.printStackTrace ();
        }
    }

    @FXML
    void btnAttachOnMouseEntered(MouseEvent event) {
        imgAttatch.setImage ( new Image ( "asset/attatch_btn_green.png" ) );
    }

    @FXML
    void btnAttachOnMouseExited(MouseEvent event) {
        imgAttatch.setImage ( new Image ( "asset/attatch_btn_grey.png" ) );
    }

    @FXML
    void btnEmojiOnAction(ActionEvent event) {
        if ( !emojiPaneVisible ) {
            emojiPaneVisible = true;
            emojiPane.setVisible ( true );
        } else {
            emojiPaneVisible = false;
            emojiPane.setVisible ( false );
            imgEmoji.setImage ( new Image ( "asset/emoji_btn_grey.png" ) );
        }
    }

    @FXML
    void btnEmojiOnMouseEntered(MouseEvent event) {
        if ( !emojiPaneVisible ) {
            imgEmoji.setImage ( new Image ( "asset/emoji_btn_green.png" ) );
        }
    }

    @FXML
    void btnEmojiOnMouseExited(MouseEvent event) {
        if ( !emojiPaneVisible ) {
            imgEmoji.setImage ( new Image ( "asset/emoji_btn_grey.png" ) );
        }
    }

    @FXML
    void btnSendOnAction(ActionEvent event) {
        handleTextOutput ( txtMessage.getText (), LocalTime.now().format( DateTimeFormatter.ofPattern("HH:mm")) );
        txtMessage.clear ();
    }

    @FXML
    void btnSendOnMouseEntered(MouseEvent event) {
        imgSend.setImage ( new Image ( "asset/send_btn_green.png" ) );
    }

    @FXML
    void btnSendOnMouseExited(MouseEvent event) {
        imgSend.setImage ( new Image ( "asset/send_btn_grey.png" ) );
    }

    @FXML
    public void txtMessageOnAction ( ActionEvent actionEvent ) {
        btnSendOnAction ( new ActionEvent () );
    }

    @FXML
    public void imgBackOnMouseClicked ( MouseEvent mouseEvent ) {
    }

    @Override
    public void initialize ( URL url, ResourceBundle resourceBundle ) {
        lblUser.setText ( username );
        loadEmojiPane ();
        autoScrollDown ();

        new Thread ( () -> {
            client.run ();
        } ).start ();
    }
}
