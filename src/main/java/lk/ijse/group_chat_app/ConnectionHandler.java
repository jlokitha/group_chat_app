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
                    String[] user = message.split ( "/" );

                    if ( user.length == 2 ) {
                        username = user[1];
                        Server.getServer ().broadcast ( user[1] + " Joined the chat !" );
                    }
                } else if (message.startsWith ( "Shutdown" )) {
                    Server.getServer ().broadcast ( username + " left !" );
                    shutdown ();
                } else {
                    System.out.println ( username + " sent message for broadcast: " + message );
                    Server.getServer ().broadcast ( username + ": " + message );
                }
            }
        } catch ( IOException e ) {
            shutdown ();
        }
    }

    public void sendMessage(String message) {
        try {
            out.writeUTF ( message );
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
