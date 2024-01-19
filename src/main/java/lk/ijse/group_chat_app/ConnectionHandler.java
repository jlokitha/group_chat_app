package lk.ijse.group_chat_app;

import java.io.*;
import java.net.Socket;

public class ConnectionHandler implements Runnable {
    private Socket client;
    private DataOutputStream out;
    private BufferedReader in;
    private String username;

    public ConnectionHandler ( Socket client ) {
        this.client = client;
    }
    @Override
    public void run () {
        try {
            in = new BufferedReader ( new InputStreamReader ( client.getInputStream () ) );
            out = new DataOutputStream ( client.getOutputStream () );

            String message;

            while ( (message = in.readLine ()) != null ) {
                System.out.println ( "message sent for server: " + message );
                if ( message.startsWith ( "Username" ) ) {
                    String[] user = message.split ( "/" );

                    if ( user.length == 2 ) {
                        username = user[1];
                        Server.getServer ().broadcast ( user[1] + "Joined the chat !" );
                    }
                } else if (message.startsWith ( "Shutdown" )) {
                    Server.getServer ().broadcast ( username + " left !" );
                    shutdown ();
                } else {
                    System.out.println ( "message for broadcast: " + message );
                    Server.getServer ().broadcast ( username + ": " + message );
                }
            }
        } catch ( IOException e ) {
            e.printStackTrace ();
        }
    }

    public void sendMessage(String message) {
        try {
            out.writeUTF ( message );
        } catch ( IOException e ) {
            e.printStackTrace ();
        }
    }

    public void shutdown() {
        try {
            in.close ();
            out.close ();
            client.close ();
        } catch ( IOException e ) {
            e.printStackTrace ();
        }
    }
}
