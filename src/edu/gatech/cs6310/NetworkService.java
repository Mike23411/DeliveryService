package edu.gatech.cs6310;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class NetworkService implements Runnable {

    public final ServerSocket serverSocket;
    protected AtomicBoolean stopThreads;

    public NetworkService(int socketPort, AtomicBoolean stopThreads) throws IOException {
        serverSocket = new ServerSocket();
        serverSocket.setReuseAddress(true);
        serverSocket.bind(new InetSocketAddress(socketPort));
        this.stopThreads = stopThreads;
    }

    public void run() {
        try {
            serverRun();
        } finally {
            try {
                serverSocket.close();
            } catch (Exception e) {
                System.out.println("ERROR:failed_closing_server_socket,exit_anyway");
            }
        }
    }

    abstract void serverRun();

}
