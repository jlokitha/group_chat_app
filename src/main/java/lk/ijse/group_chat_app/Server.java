package lk.ijse.group_chat_app;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {
    private static Server server;
    private ServerSocket serverSocket;
    private ArrayList<ConnectionHandler> connectionList;
    private ExecutorService threadPool;

    private Server() {
        connectionList = new ArrayList<> ();
    }

    @Override
    public void run () {
        try {
            serverSocket = new ServerSocket ( 5000 );
            threadPool = Executors.newCachedThreadPool ( );

            while ( true ) {
                Socket client = serverSocket.accept ( );
                System.out.println ( "A client connected !" );
                ConnectionHandler connection = new ConnectionHandler ( client );
                connectionList.add ( connection );
                threadPool.execute ( connection );
            }

        } catch ( IOException e ) {
            e.printStackTrace ();
        }
    }

    public void broadcastInfo ( String sender, String message ) {
        for (ConnectionHandler client : connectionList) {
            if ( client != null ) {
                client.sendInfo ( sender, message );
            }
        }
    }

    public void broadcastText ( String sender, String message, String time) {
        for (ConnectionHandler client : connectionList) {
            if ( client != null ) {
                client.sendMessage ( sender, message, time );
            }
        }
    }

    public void broadcastImage ( String sender, byte[] imageData, String time ) {
        for (ConnectionHandler client : connectionList) {
            if ( client != null ) {
                client.sendImage ( sender, imageData, time );
            }
        }
    }

    public static Server getServer() {
        return (server == null) ? server = new Server () : server;
    }

    public static void main ( String[] args ) {
        Server.getServer ().run ();
    }
}
