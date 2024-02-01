package lk.ijse.group_chat_app.controller;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
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
    public JFXButton btnEmoji;

    @FXML
    public JFXButton btnSend;

    @FXML
    public JFXButton btnAttach;

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
                     sender = (sender.equals ( username )) ? "You" : sender;
                     String msg = in.readUTF ();
                     int onlineCount = in.readInt ();

                     setInfoToVbox ( sender, msg );
                     setOnlineCount ( onlineCount );

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

    //Update online user count
    private void setOnlineCount ( int onlineCount ) {
        Platform.runLater ( () -> {
            lblOnlineCount.setText ( onlineCount + " Online" );
        } );
    }

    //Set information to vbox
    public void setInfoToVbox( String sender, String message) {
        Platform.runLater ( ( ) -> {

            Text text = new Text ( sender + " " + message );
            text.setFont ( Font.font ( 15 ) );
            TextFlow textFlow = new TextFlow ( text );
            HBox hBox = new HBox ( );
            hBox.getChildren ().add ( textFlow );
            hBox.setAlignment ( Pos.CENTER );
            hBox.setPrefWidth ( 962 );
            vbox.getChildren ().add ( hBox );

        } );
    }

    //Invoke correct method based on sender of the message
    public void setTextToVbox(String sender, String message, String time) {
        Platform.runLater ( ( ) -> {

            if ( !sender.equals ( username ) ) {
                setOtherMessage ( sender, message, time );
            } else {
                setMyMessage ( message, time );
            }

        } );
    }

    //Set text messages sent by other users
    public void setOtherMessage ( String sender, String message, String time ) {
        try {
            FXMLLoader loader = new FXMLLoader ( ChatRoomFormController.class.getResource ( "/view/whiteTextBubbleForm.fxml" ) );
            Parent root = loader.load ( );
            WhiteTextBubbleFormController controller = loader.getController ( );
            controller.setData ( sender, message, time );
            vbox.getChildren ( ).add ( root );
            autoScrollDown ();
        } catch ( IOException e ) {
            e.printStackTrace ();
        }
    }

    //Set text messages sent by user
    public void setMyMessage (String message, String time) {
        try {
            FXMLLoader loader = new FXMLLoader ( ChatRoomFormController.class.getResource ( "/view/greenTextBubbleForm.fxml" ) );
            Parent root = loader.load ( );
            GreenTextBubbleFormController controller = loader.getController ( );
            controller.setData ( message, time );
            vbox.getChildren ( ).add ( root );
            autoScrollDown ();
        } catch ( IOException e ) {
            e.printStackTrace ();
        }
    }

    //Set images to vbox according to sender
    public void setImgToVbox( String sender, byte[] imageData, String time ) {
        Platform.runLater(() -> {

            try {
                if ( !sender.equals ( username ) ) {

                    FXMLLoader loader = new FXMLLoader ( ChatRoomFormController.class.getResource ( "/view/whiteImagePaneForm.fxml" ) );
                    Parent root = loader.load ( );
                    WhiteImagePaneFormController controller = loader.getController ( );
                    controller.setData ( sender, new Image ( new ByteArrayInputStream ( imageData ) ), time );
                    vbox.getChildren ( ).add ( root );
                    autoScrollDown ( );

                } else {

                    FXMLLoader loader = new FXMLLoader ( ChatRoomFormController.class.getResource ( "/view/greenImagePaneForm.fxml" ) );
                    Parent root = loader.load ( );
                    GreenImagePaneFormController controller = loader.getController ( );
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

    //Minimize chat UI
    @FXML
    public void imgMinimizeOnMouseClicked ( MouseEvent mouseEvent ) {
        Stage stage = ( Stage) ((ImageView)mouseEvent.getSource ()).getScene ().getWindow ();
        stage.setIconified ( true );
    }

    //Notify server side that user exit the app and close socket and data input / output stream
    public void shutdown() {
        try {
            out.writeUTF ( "/info" );
            out.flush ();

            out.writeUTF ( "Shutdown" );
            out.flush ();

            in.close ();
            out.close ();

            if ( !remoteSocket.isClosed () ) {
                remoteSocket.close ();
            }
        } catch ( IOException e ) {
            // Ignore
        }
    }

    //Add unicode of emoji to text field
    public void setEmojiToTxt (String unicode) {
        txtMessage.appendText ( unicode );
    }

    //Create new emoji pane object and set it to anchor pane
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
            if ( ! message.startsWith ( "Username" ) ) {

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

    //Scroll to bottom of the scroll pane
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
            //Create new file chooser and add extension filter so other than images can't be selected

            FileChooser fileChooser = new FileChooser ( );
            fileChooser.setTitle ( "Choose Image File" );

            fileChooser.getExtensionFilters ( ).addAll (
                    new FileChooser.ExtensionFilter ( "Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif" )
            );

            //Save selected image as file
            File file = fileChooser.showOpenDialog ( txtMessage.getScene ( ).getWindow ( ) );

            if ( file != null ) {
                //Convert selected image to byte[]
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
        if ( !txtMessage.getText ().isEmpty () ) {
            handleTextOutput ( txtMessage.getText (), LocalTime.now().format( DateTimeFormatter.ofPattern("HH:mm")) );
            txtMessage.clear ();
        }
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
        try {
            //Close socket and data input / output stream before going to login page
            shutdown ();

            Parent parent = FXMLLoader.load ( this.getClass ().getResource ( "/view/loginForm.fxml" ) );
            Stage stage = (Stage) ((Node) mouseEvent.getSource ()).getScene ().getWindow ();
            stage.setScene ( new Scene ( parent ) );
            stage.centerOnScreen ();
            stage.show ();
        } catch ( IOException e ) {
            e.printStackTrace ();
        }
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
