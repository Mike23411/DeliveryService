package edu.gatech.cs6310;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class DeliveryService implements Runnable {

    Socket socket;

    DeliveryService(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        Scanner commandLineInput = new Scanner(System.in);
        String wholeInputLine;
        String[] tokens;
        final String DELIMITER = ",";

        try {
            DeliveryManager dm = new DeliveryManager();
            // Get the outputstream of client
            PrintWriter outS = new PrintWriter(
                    socket.getOutputStream(), true);
            // Get the inputstream of client
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()));
            // Determine the next command and echo it to the monitor for testing purposes
            wholeInputLine = in.readLine();
            System.out.println(" the whole input line is: " + wholeInputLine);
            tokens = wholeInputLine.split(DELIMITER);
            System.out.println("> " + wholeInputLine);
            String returnString = null;

            try {
                if (tokens[0].equals("make_store")) {
                    returnString = dm.makeStore(tokens[1], Integer.valueOf(tokens[2]));

                } else if (tokens[0].equals("display_stores")) {
                    String out = dm.displayStores();
                    StringBuilder sb = new StringBuilder();
                    if (out != null) {
                        sb.append(out);
                    }
                    sb.append("OK:display_completed");
                    returnString = sb.toString();

                } else if (tokens[0].equals("sell_item")) {
                    returnString = dm.sellItem(tokens[1], tokens[2], Integer.valueOf(tokens[3]));

                } else if (tokens[0].equals("display_items")) {
                    String out = dm.displayItems(tokens[1]);

                    StringBuilder sb = new StringBuilder();
                    if (out != null) {
                        sb.append(out);
                    }
                    returnString = sb.toString();

                } else if (tokens[0].equals("make_pilot")) {
                    returnString = dm.makePilot(tokens[1], tokens[2], tokens[3],
                            tokens[4], tokens[5], tokens[6], Integer.valueOf(tokens[7]));

                } else if (tokens[0].equals("display_pilots")) {
                    String out = dm.displayPilots();

                    StringBuilder sb = new StringBuilder();
                    if (out != null) {
                        sb.append(out);
                    }
                    sb.append("OK:display_completed");
                    returnString = sb.toString();

                } else if (tokens[0].equals("make_drone")) {
                    returnString =
                            dm.makeDrone(tokens[1], Integer.valueOf(tokens[2]),
                                    Integer.valueOf(tokens[3]), Integer.valueOf(tokens[4]));

                } else if (tokens[0].equals("display_drones")) {
                    String out = dm.displayDrones(tokens[1]);

                    StringBuilder sb = new StringBuilder();
                    if (out != null) {
                        sb.append(out);
                    }
                    returnString = sb.toString();

                } else if (tokens[0].equals("fly_drone")) {
                    returnString = dm.flyDrone(tokens[1], Integer.valueOf(tokens[2]), tokens[3]);

                } else if (tokens[0].equals("make_customer")) {
                    returnString = dm.makeCustomer(tokens[1], tokens[2], tokens[3],
                            tokens[4], Integer.valueOf(tokens[5]), Integer.valueOf(tokens[6]));
                } else if (tokens[0].equals("display_customers")) {
                    String out = dm.displayCustomers();

                    StringBuilder sb = new StringBuilder();
                    if (out != null) {
                        sb.append(out);
                    }
                    sb.append("OK:display_completed");
                    returnString = sb.toString();
                } else if (tokens[0].equals("start_order")) {
                    returnString = dm.startOrder(tokens[1], tokens[2], Integer.valueOf(tokens[3]), tokens[4]);

                } else if (tokens[0].equals("display_orders")) {
                    String out = dm.displayOrders(tokens[1]);

                    StringBuilder sb = new StringBuilder();
                    if (out != null) {
                        sb.append(out);
                    }
                    returnString = sb.toString();

                } else if (tokens[0].equals("request_item")) {
                    returnString =
                            dm.requestItem(
                                    tokens[1], tokens[2], tokens[3], Integer.valueOf(tokens[4]), Integer.valueOf(tokens[5])
                            );

                } else if (tokens[0].equals("purchase_order")) {
                    returnString = dm.purchaseOrder(tokens[1], tokens[2]);

                } else if (tokens[0].equals("cancel_order")) {
                    returnString = dm.cancelOrder(tokens[1], tokens[2]);

                } else if (tokens[0].equals("stop")) {
                    returnString = "stop_acknowledged";

                } else if (tokens[0].equals("clean_up")) {
                    dm.cleanUp();
                    returnString = "OK:data_archived";

                }
                /*
                else if (tokens[0].equals("kill_connections")) {

                    dm.killAll();
                    returnString = "OK:connections_killed";

                } */
                else {
                    returnString = "command " + tokens[0] + " NOT acknowledged";
                }
            } catch (Exception e)
            {
                System.out.println(e.toString());
                returnString = "ERROR:Input_Not_Accepted";
            }

            Integer retSize = returnString.length();
            String retSizeString = retSize.toString();
            if (retSizeString.length() <= 20) {
                retSizeString = StringUtilities.padLeftSpaces(retSizeString, 20);
                outS.write(retSizeString);
                outS.write(returnString);
                outS.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println();
        }
    }

}
