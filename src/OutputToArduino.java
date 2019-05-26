
public class OutputToArduino implements Runnable { // implements Runnable to work with threads.
    private ComPort comPort;
    private Huiscentrale huiscentrale;

    // constructor that gives this class the same instance of comport as all other serialport communicating classes.
    // it will also get an instance of Huiscentrale.
    OutputToArduino(ComPort comPort, Huiscentrale huiscentrale) {
        this.comPort = comPort;
        this.huiscentrale = huiscentrale;
    }


    // the run method is used for multithreading, if this thread starts it kicks off run.
    @Override
    public void run() {
        // keep listening for server input as long as the thread is alive.
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
                System.exit(0);
                break;
            }
            System.out.println("received from server: " + request);
            sendToArduino(request);
        }
    }

 void sendToArduino(String message) {
        String[] messageSplit = message.split(";", 0);
        //checks if the message has the correct format
        if (messageSplit.length == 3) {
            String serverCommand = messageSplit[0];
            String outputFor = messageSplit[1];


            String outputToArduino = messageSplit[2];

            System.out.println("Sending to Arduino: " + outputToArduino);
            try {
                comPort.writeOutput(outputToArduino);
            } catch (Exception e) {
                System.out.println("protocol has incorrect format" + e);

            }
        }
        else {
            //should never happen but is put in just to be sure
            System.out.println("message is either too long or too short");

        }

    }

}