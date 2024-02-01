package lk.ijse.group_chat_app.server;

import java.io.*;
import java.net.Socket;

public class ConnectionHandler implements Runnable {
    private Socket client;
    private DataOutputStream out;
    private DataInputStream in;
    private String username;

    public ConnectionHandler ( Socket client ) {
        this.client = client;
    }
    @Override
    public void run () {
        try {
            in = new DataInputStream ( new BufferedInputStream ( client.getInputStream () ) );
            out = new DataOutputStream ( client.getOutputStream () );

            String message;

            while ( (message = in.readUTF ()) != null ) {

                //Handle information
                if (message.equals ( "/info" )) {

                    message = in.readUTF ();

                    if ( message.startsWith ( "Username" ) ) {
                        String[] user = message.split ( " " );


                        if ( user.length == 2 ) {
                            username = user[1];
                            Server.getServer ().broadcastInfo ( username, " Joined the chat !" );
                        }

                    } else {

                        Server.getServer ().removeConnectionHandler ( this );
                        Server.getServer ().broadcastInfo ( username, " left !" );
                        shutdown ();

                    }
                //Handle text messages
                } else if ( message.equals ( "/txt" ) ){

                    String msg = in.readUTF ();
                    String time = in.readUTF ();

                    Server.getServer ().broadcastText ( username, msg, time );

                //Handle images
                } else if ( message.equals ( "/img" ) ) {

                    int imageLength = in.readInt ( );

                    byte[] imageData = new byte[imageLength];
                    in.readFully ( imageData );

                    String time = in.readUTF ();

                    Server.getServer ().broadcastImage ( username, imageData, time );
                }
            }
        } catch ( IOException e ) {
            shutdown ();
        }
    }

    public void sendInfo(String sender, String message, int onlineCount ) {
        try {
            out.writeUTF ( "/info" );
            out.flush ();

            out.writeUTF ( sender );
            out.flush ();

            out.writeUTF ( message );
            out.flush ();

            out.writeInt ( onlineCount );
            out.flush ();

        } catch ( IOException e ) {
            e.printStackTrace ();
        }
    }

    public void sendMessage(String sender, String message, String time ) {
        try {
            out.writeUTF ( "/txt" );
            out.flush ();

            out.writeUTF ( sender );
            out.flush ();

            out.writeUTF ( message );
            out.flush ();

            out.writeUTF ( time );
            out.flush ();

        } catch ( IOException e ) {
            e.printStackTrace ();
        }
    }

    public void sendImage( String sender, byte[] imageData, String time ) {
        try {
            out.writeUTF ( "/img" );
            out.flush ();

            out.writeUTF ( sender );
            out.flush ();

            out.writeInt ( imageData.length );
            out.flush ();

            out.write ( imageData );
            out.flush ();

            out.writeUTF ( time );
            out.flush ();

        } catch ( IOException e ) {
            e.printStackTrace ();
        }
    }

    //Close socket and data input / output stream when user exit
    public void shutdown() {
        try {
            in.close ();
            out.close ();

            if ( !client.isClosed () ) {
                client.close ( );
            }
        } catch ( IOException e ) {
            // Ignore
        }
    }
}
