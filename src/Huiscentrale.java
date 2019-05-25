import com.onsdomein.proxy.ProxyOnsDomein;

import java.io.IOException;

class Huiscentrale {
    private String client_id = "5678";
    private ProxyOnsDomein proxy = new ProxyOnsDomein();

    Huiscentrale() {
        huiscentraleInitialize();
    }



    public void huiscentraleInitialize() {
        // Instantiate and initialize a comport via class ComPort.
        ComPort comPort = new ComPort();
        comPort.initialize();

        // Create instances of arduinoOut and arduinoIn. Both get the same instance of comPort so that they communicate via the same line.
        ArduinoOut arduinoOut = new ArduinoOut(comPort, proxy);
        ArduinoIn arduinoIn = new ArduinoIn(comPort, proxy);

        // Start two threads for each class that needs one.
        Thread t1 = new Thread(arduinoOut);
        Thread t2 = new Thread(arduinoIn);


        // After first boot of app connection is made with both arduino and server, then passes on to listeningForMessage
        try {
            proxy.connectClientToServer(client_id);
            System.out.println("Connected to server");
            t1.start();
            t2.start();

        } catch (
                IOException e) {
            System.out.println("HC cannot connect to server: " + e);

        }


    }

}
