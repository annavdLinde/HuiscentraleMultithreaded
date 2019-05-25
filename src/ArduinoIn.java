import com.onsdomein.proxy.ProxyOnsDomein;

public class ArduinoIn implements Runnable{ // implements Runnable to work with threads.
    private ComPort comPort;
    private ProxyOnsDomein proxy;



    // constructor that gives this class the same instance of comport as all other serialport communicating classes.
    ArduinoIn(ComPort comport, ProxyOnsDomein proxy) {
        this.comPort = comport;
        this.proxy = proxy;
    }

    // the run method is used for multithreading, if this thread starts it kicks off run.
    @Override
    public void run() {
        // As long as the thread is alive this while loop will be alive and will keep listening to what the Arduino has to say.
        String inputBuffer;
        while (!Thread.interrupted()) {
            inputBuffer = comPort.readInput();
            if (inputBuffer.startsWith("<") && inputBuffer.endsWith(">")) {
                sendToProxy("1234", inputBuffer);
            }
            else {
                System.out.println(inputBuffer);
            }



        }
    }

    private void sendToProxy(String reactionFor, String reactionFromArduino) {
        try {
            //TODO: make sure you always respond, the server will if HC is offline, GA waits for a reply from either server or HC
            //TODO: change hardcoded "setHc" to string that will be filled by the first part of the incoming message from the server
            //TODO: make sure client_id is found (located in huiscentrale class) instead of hardcoded id

            proxy.sendResponse("setHc", "5678", reactionFor, reactionFromArduino);
        } catch (Exception e) {
            System.out.println("HC kan geen contact maken met de server. " + e);
        }
    }
}
