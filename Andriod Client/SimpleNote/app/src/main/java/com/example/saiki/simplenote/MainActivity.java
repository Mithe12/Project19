package com.example.saiki.simplenote;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    EditText GetIP_Address ;
    EditText GetPort;
    Socket serverSocket = null;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GetIP_Address = (EditText)findViewById(R.id.editText);
        GetPort       = (EditText)findViewById(R.id.editText2);

    }
    public void connectServer(View view){

        try{
            String serAdd = GetIP_Address.getText().toString();
            int port = Integer.parseInt(GetPort.getText().toString());

            ServerConnection myserver =  new ServerConnection(serAdd,port);
            myserver.execute();
            serverSocket = myserver.getClientSocketds();

            if(serverSocket != null) {
                Intent i = new Intent(this, MainScreen.class);
                startActivity(i);
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
