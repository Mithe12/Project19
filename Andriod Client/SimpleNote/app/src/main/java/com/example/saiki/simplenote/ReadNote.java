package com.example.saiki.simplenote;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ReadNote extends AppCompatActivity {
    EditText readNotes;
    Socket serverSocket = null;
    PrintWriter out = null;
    BufferedReader in = null;
    String msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_note);
        readNotes = (EditText) findViewById(R.id.editText5);
    }
    public void connectWriteNote(View view){

        try {
            ServerConnection myserver01 = new ServerConnection();
            serverSocket = myserver01.getClientSocketds();
            out = new PrintWriter(serverSocket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
            if (serverSocket != null) {

                out.println("2");
                out.flush();
                msg = in.readLine();

                out.println(readNotes);
                Intent i = new Intent(this, ReadContentNote.class);
                startActivity(i);
            }

        } catch (IOException e) {
            Intent i = new Intent(this, ServerConnection.class);
            startActivity(i);
        }
    }
}
