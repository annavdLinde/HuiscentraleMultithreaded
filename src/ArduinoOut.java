public class ArduinoOut implements Runnable { // implements Runnable to work with threads.
    private ComPort comPort;


    // In this class we want access to the threads for stopping them. Not sure if this is necessary.
    private Huiscentrale huiscentrale;

    // constructor that gives this class the same instance of comport as all other serialport communicating classes.
    // it will also get an instance of Huiscentrale.
    public ArduinoOut(ComPort comPort, Huiscentrale huiscentrale) {
        this.comPort = comPort;
        this.huiscentrale = huiscentrale;
    }


    // the run method is used for multithreading, if this thread starts it kicks off run.
    @Override
    public void run() {
        // keep listening to console input as long as the thread is alive.
        while ( !Thread.interrupted()) {
            listenForMessageFromServer();

        }
    }

    private void listenForMessageFromServer() {
        // TODO: make sure the program breaks out of the while true loop when the connection with the server is lost
        // get messages from server
        while (true) {
            String request;
            try {
                request = huiscentrale.proxy.receiveRequest();
            } catch (Exception e) {
                System.out.println("Connection with server lost. " + e);
                break;
            }
            System.out.println("received from server: " + request);
            sendToArduino(request);
        }
    }

    private void sendToArduino(String message) {
        String[] messageSplit = message.split(",", 0);
        //checks if the message has the correct format
        if (messageSplit.length == 3) {

            String outputToArduino = messageSplit[2];

            System.out.println("Sending to Arduino: " + outputToArduino);
            try {
                comPort.writeOutput(outputToArduino);
            } catch (Exception e) {
                System.out.println("protocol has incorrect format" + e);

            }
        }
        else {
            System.out.println("message has incorrect format");
            //TODO: message needs to be send back to proxy with this conclusion
        }

    }
}