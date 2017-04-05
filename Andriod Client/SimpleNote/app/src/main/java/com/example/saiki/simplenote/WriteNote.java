package com.example.saiki.simplenote;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class WriteNote extends AppCompatActivity {

    TextView Get_notes ;
    Socket serverSocket = null;;
    PrintWriter out = null;
    BufferedReader in = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_note);

        Get_notes = (TextView)findViewById(R.id.textView6);
    }
    public void connectServer(View view){

        try{
            ServerConnection myserver02 =  new ServerConnection();
            serverSocket = myserver02.getClientSocketds();
            out = new PrintWriter(serverSocket.getOutputStream());
            in = new BufferedReader( new InputStreamReader(serverSocket.getInputStream()));
            if(serverSocket != null) {
                if(Get_notes != null){
                    out.println(Get_notes);
                    out.flush();
                    out.println("vola");
                    out.flush();
                }
                else {

                    out.println("vola");
                    out.flush();;
                }
            }else {
                Intent i = new Intent(this, ServerConnection.class);
                startActivity(i);
            }
        }catch(IOException e){
            Intent i = new Intent(this, ServerConnection.class);
            startActivity(i);
        }

    }
}
