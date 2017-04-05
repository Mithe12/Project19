package com.example.saiki.simplenote;

import android.content.Intent;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * Created by Mithi on 26-03-2017.
 */

public class ServerConnection extends AsyncTask<Void,String,Void> {

    public static Socket clientSocketds;
    private int port;
    private String serAddr;

    ServerConnection(){

    }

    ServerConnection(String seraddr, int port) throws IOException {
        this.serAddr = seraddr;
        this.port = port;

    }

    public Socket getClientSocketds() {
        return clientSocketds;
    }

    @Override
    protected Void doInBackground(Void... parameter) {
        try {
            this.clientSocketds = new Socket(this.serAddr, this.port);
        } catch (UnknownHostException e1) {
            return null;
        } catch (IOException e1) {
            return null;
        }
        return null ;
    }

}

