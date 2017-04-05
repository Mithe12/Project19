package com.example.saiki.simplenote;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ReadContentNote extends AppCompatActivity {

    TextView readNotes;
    Socket serverSocket = null;
    PrintWriter out = null;
    BufferedReader in = null;
    String msg;
    String readbuffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_content_note);
        readNotes = (TextView) findViewById(R.id.textView6);
        try {
            ServerConnection myserver02 = new ServerConnection();
            serverSocket = myserver02.getClientSocketds();
            if(serverSocket != null) {
                out = new PrintWriter(serverSocket.getOutputStream());
                in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
                msg = in.readLine();
                if (msg.equals("No File Founded")) {
                   readNotes.setText(msg);
                } else {
                    while (true) {
                        if (msg.equals("End of the file")) {
                            break;
                        }
                        readbuffer = readbuffer.concat(msg);
                    }
                    readNotes.setText(msg);
                }
            }
        }catch(IOException e){
            Intent i = new Intent(this, ServerConnection.class);
            startActivity(i);

        }
    }
}

