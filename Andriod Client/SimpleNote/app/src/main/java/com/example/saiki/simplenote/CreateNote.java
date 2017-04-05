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

public class CreateNote extends AppCompatActivity {

    EditText create_notes;
    Socket serverSocket = null;
    PrintWriter out = null;
    BufferedReader in = null;
    String msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        create_notes = (EditText) findViewById(R.id.editText5);

    }

    public void connectWriteNote(View view) {

        try {
            ServerConnection myserver01 = new ServerConnection();
            serverSocket = myserver01.getClientSocketds();
            out = new PrintWriter(serverSocket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
            if (serverSocket != null) {

                out.println("1");
                out.flush();
                msg = in.readLine();

                out.println(create_notes);
                msg = in.readLine();
                if (msg != null) {
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(this, WriteNote.class);
                    startActivity(i);
                } else {
                    Toast.makeText(this, "Note not created", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (IOException e) {
            Intent i = new Intent(this, ServerConnection.class);
            startActivity(i);
        }

    }

}
