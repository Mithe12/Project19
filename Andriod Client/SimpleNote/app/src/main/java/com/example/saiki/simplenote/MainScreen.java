package com.example.saiki.simplenote;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MainScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
    }

    public void connectCreateNote(View view){
        Intent i = new Intent(this,CreateNote.class);
        startActivity(i);
    }

    public void connectReadNote(View view){
        Intent i = new Intent(this,ReadNote.class);
        startActivity(i);
    }
    public void connectDeleteNote(View view){
        Intent i = new Intent(this,DeleteNote.class);
        startActivity(i);
    }

    public void connectDisplayNote(View view){
        Intent i = new Intent(this,DisplayNotes.class);
        startActivity(i);
    }

    public void exit(View view){
        ServerConnection myserver02 = new ServerConnection();
        Socket serverSocket = myserver02.getClientSocketds();
        if(serverSocket == null){
            finish();
        }
        else {
            try {
                PrintWriter out = new PrintWriter(serverSocket.getOutputStream());
                BufferedReader in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
                out.println("5");
                finish();
            } catch (IOException e) {
                finish();
            }
        }
    }
}
