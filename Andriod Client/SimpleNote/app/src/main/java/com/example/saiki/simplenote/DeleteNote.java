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

public class DeleteNote extends AppCompatActivity {

    EditText deleteNotes;
    Socket serverSocket = null;
    PrintWriter out = null;
    BufferedReader in = null;
    String msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_note);
        deleteNotes = (EditText) findViewById(R.id.editText5);
    }
    public void noteDelete(View view){

        try {
            ServerConnection myserver01 = new ServerConnection();
            serverSocket = myserver01.getClientSocketds();
            if (serverSocket != null) {
                out = new PrintWriter(serverSocket.getOutputStream());
                in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
                out.println("3");
                out.flush();

                msg = in.readLine();
                out.println(deleteNotes);
                msg = in.readLine();

                if (msg.equals("File is deleted")) {
                    Intent intt = new Intent(this, NoteDelete.class);
                    startActivity(intt);
                }else{
                    Toast.makeText(this, "No note present", Toast.LENGTH_SHORT).show();
                }

            }
        } catch (IOException e) {
            Intent it = new Intent(this, ServerConnection.class);
            startActivity(it);
        }
    }
}