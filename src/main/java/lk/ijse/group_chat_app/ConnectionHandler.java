package lk.ijse.group_chat_app;

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

                if ( message.startsWith ( "Username" ) ) {
                    String[] user = message.split ( " " );


                    if ( user.length == 2 ) {
                        username = user[1];
                        Server.getServer ().broadcastText ( user[1] + ":Joined the chat !" );
                    }

                } else if (message.startsWith ( "Shutdown" )) {

                    Server.getServer ().broadcastText ( username + " left !" );
                    shutdown ();

                } else if ( message.startsWith ( "/txt" ) ){

                    Server.getServer ().broadcastText ( username + ":" + in.readUTF () );

                } else if ( message.startsWith ( "/img" ) ) {

                    int imageLength = in.readInt ( );

                    byte[] imageData = new byte[imageLength];
                    in.readFully ( imageData );

                    Server.getServer ().broadcastImage ( username, imageData );
                }
            }
        } catch ( IOException e ) {
            shutdown ();
        }
    }

    public void sendMessage(String message) {
        try {
            out.writeUTF ( "/txt" );
            out.flush ();

            out.writeUTF ( message );
            out.flush ();
        } catch ( IOException e ) {
            e.printStackTrace ();
        }
    }

    public void sendImage( String sender, byte[] imageData ) {
        try {
            out.writeUTF ( "/img" );
            out.flush ();

            out.writeInt ( imageData.length );
            out.flush ();

            out.writeUTF ( sender );
            out.flush ();

            out.write ( imageData );
            out.flush ();
        } catch ( IOException e ) {
            e.printStackTrace ();
        }
    }

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
