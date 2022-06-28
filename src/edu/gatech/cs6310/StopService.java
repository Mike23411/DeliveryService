package edu.gatech.cs6310;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class StopService extends NetworkService {

    public StopService(int socketPort, AtomicBoolean stopThreads) throws IOException {
        super(socketPort, stopThreads);
    }

    @Override
    void serverRun() {
        try {
            while (true) {
                Socket s = serverSocket.accept();

                // Get the outputstream of client
                PrintWriter outS = new PrintWriter(
                        s.getOutputStream(), true);

                // Get the inputstream of client
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                s.getInputStream()));
                String string = in.readLine();
                if (string != null) {
                    stopThreads.set(true);
                    String returnString = "Stopping DeliveryService";
                    Integer retSize = returnString.length();
                    String retSizeString = retSize.toString();
                    if (retSizeString.length() <= 20) {
                        System.out.println(retSizeString);
                        retSizeString = StringUtilities.padLeftSpaces(retSizeString, 20);
                        outS.write(retSizeString);
                        outS.write(returnString);
                        outS.flush();
                    }
                    s.close();
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

}
