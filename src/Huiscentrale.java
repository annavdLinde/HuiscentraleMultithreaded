import com.onsdomein.proxy.ProxyOnsDomein;

import java.io.IOException;

class Huiscentrale {
    private String client_id = "5678";
    private String serverCommand;
    private String requestForId;
    ProxyOnsDomein proxy = new ProxyOnsDomein();

    ComPort comPort = new ComPort();

    // Create instances of outputToArduino and inputFromArduino. Both get the same instance of comPort so that they communicate via the same line.
    OutputToArduino outputToArduino = new OutputToArduino(comPort, this);
    InputFromArduino inputFromArduino = new InputFromArduino(comPort, this);

    // Start two threads, one for each class that needs one.
    Thread t1 = new Thread(outputToArduino);
    Thread t2 = new Thread(inputFromArduino);


    Huiscentrale() {
        huiscentraleInitialize();
    }



    private void huiscentraleInitialize() {
        // Instantiate and initialize a comport via class ComPort.
        comPort.initialize();

        // After first boot of app connection is made with both Arduino and server, then passes on to listeningForMessage
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

    void setServercommand(String command) {
        this.serverCommand = command;
    }

    String getServercommand() {
        return this.serverCommand;
    }

    void setRequestForId (String id){
        this.requestForId = id;
    }

    String getRequestForId(){
        return this.requestForId;
    }

    String getClient_id () {
        return this.client_id;
    }


}
