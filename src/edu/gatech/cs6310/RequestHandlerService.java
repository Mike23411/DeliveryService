package edu.gatech.cs6310;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class RequestHandlerService extends NetworkService {

    private final ExecutorService threadPool;

    public RequestHandlerService(int socketPort, AtomicBoolean stopThreads, int numThreads) throws IOException {
        super(socketPort, stopThreads);
        this.threadPool = Executors.newFixedThreadPool(numThreads);
    }

    @Override
    void serverRun() {
        try {
            while (true) {
                Socket s = serverSocket.accept();
                if (stopThreads.get()) {
                    s.close();
                    try {
                        stopThread();
                    } catch (Exception e) {
                        System.out.println("RequestHandlerService: " + e);
                    }
                    return;
                }
                DeliveryService d = new DeliveryService(s);
                threadPool.execute(d);
            }
        } catch (IOException e) {
            System.out.println("ERROR:WE_BROKE_EXECUTING: " + e);
        }
    }

    // Used by parent class to gracefully perform cleanup
    private void stopThread() throws InterruptedException {
        try {
            threadPool.awaitTermination(6, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw e;
        }
    }

}
