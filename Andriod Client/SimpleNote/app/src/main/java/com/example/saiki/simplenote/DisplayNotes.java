package com.example.saiki.simplenote;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class DisplayNotes extends AppCompatActivity {

    TextView display;
    Socket serverSocket = null;
    PrintWriter out = null;
    BufferedReader in = null;
    String msg;
    String readbuffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_notes);
        display = (TextView) findViewById(R.id.textView);
        try {
            ServerConnection myserver02 = new ServerConnection();
            serverSocket = myserver02.getClientSocketds();
            if(serverSocket != null) {
                out = new PrintWriter(serverSocket.getOutputStream());
                in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
                out.println("4");
                while (true) {
                        msg = in.readLine();
                        if (msg.equals("End of the file")) {
                            break;
                        }
                        readbuffer = readbuffer.concat(msg);
                    }
                    display.setText(msg);
                }
        }catch(IOException e){
            Intent i = new Intent(this, ServerConnection.class);
            startActivity(i);

        }
    }
}
