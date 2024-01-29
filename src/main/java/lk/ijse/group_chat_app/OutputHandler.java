package lk.ijse.group_chat_app;

import java.io.DataOutputStream;
import java.io.IOException;

public class OutputHandler {
    private DataOutputStream out;

    public OutputHandler ( DataOutputStream out ) {
        this.out = out;
    }

    public void handleTextOutput ( String message, String time) throws IOException {
        if ( !message.startsWith ( "Username" ) && !message.startsWith ( "Shutdown" )) {

            out.writeUTF ( "/txt" );
            out.flush ( );

            out.writeUTF ( message );
            out.flush ( );

            out.writeUTF ( time );
            out.flush ();

        } else {

            out.writeUTF ( message );
            out.flush ();

        }
    }

    public void handleImageOutput ( byte[] imageBytes) throws IOException {
        out.writeUTF("/img");
        out.flush();

        out.writeInt(imageBytes.length);
        out.flush ();

        out.write(imageBytes);
        out.flush();
    }
}
