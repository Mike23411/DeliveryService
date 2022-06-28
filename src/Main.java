import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

import edu.gatech.cs6310.NetworkService;
import edu.gatech.cs6310.RequestHandlerService;
import edu.gatech.cs6310.StopService;

public class Main {

    public static void main(String[] args) {
        try {
            AtomicBoolean stopThread = new AtomicBoolean(false);

            int numThreads = 8;

            NetworkService networkService = new RequestHandlerService(5000, stopThread, numThreads);

            System.out.println("Welcome to the Grocery Express Delivery Service!");
            Thread t = new Thread(networkService);
            t.start();

            NetworkService stopService = new StopService(6000, stopThread);
            Thread t2 = new Thread(stopService);
            t2.start();
            t.join();
            t2.join();
            System.exit(1);
        } catch (Exception exception) {
            System.out.println("ERROR:WE_BROKE: " + exception.toString());
        }
    }

}
