import com.onsdomein.proxy.ProxyOnsDomein;

public class InputFromArduino implements Runnable{ // implements Runnable to work with threads.
    private ComPort comPort;
    private Huiscentrale huiscentrale;


    // constructor that gives this class the same instance of comport as all other serialport communicating classes.
    InputFromArduino(ComPort comport, Huiscentrale huiscentrale) {
        this.comPort = comport;
        this.huiscentrale = huiscentrale;
    }

    // the run method is used for multithreading, if this thread starts it kicks off run.
    @Override
    public void run() {
        // As long as the thread is alive this while loop will be alive and will keep listening to what the Arduino has to say.
        String inputBuffer;

        while (!Thread.interrupted()) {
            inputBuffer = comPort.readInput();
            if (inputBuffer.startsWith("<") && inputBuffer.endsWith(">")) {
                System.out.println("Received from Arduino: " + inputBuffer);

                sendToProxy(inputBuffer);
            }
            else {
                System.out.println(inputBuffer);
            }



        }
    }

    void sendToProxy(String reactionFromArduino) {


        try {
            //TODO: make sure you always respond, the server will if HC is offline, GA waits for a reply from either server or HC.
            //TODO: change hardcoded "setHc" to string that will be filled by the first part of the incoming message from the server.
            //TODO: make sure requestForId is used instead of hardcoded id.
            System.out.println("Sending to server: " + "setHc;" + huiscentrale.getClient_id() + ";" + "1234" + ";" + reactionFromArduino);
            huiscentrale.proxy.sendResponse("setHc", huiscentrale.getClient_id(), "1234", reactionFromArduino);
        } catch (Exception e) {
            System.out.println("HC cannot connect server. " + e);
        }
    }
}
