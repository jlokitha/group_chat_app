package lk.ijse.group_chat_app;

import java.io.DataOutputStream;
import java.io.IOException;

public class InputHandler implements Runnable{
    private DataOutputStream out;

    public InputHandler( DataOutputStream out ) {
        this.out = out;
    }

    public void handleInput( String message) throws IOException {
        out.writeUTF ( message );
        out.flush ();
    }

    @Override
    public void run () {

    }
}
