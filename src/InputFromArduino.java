public class InputFromArduino implements Runnable { // implements Runnable to work with threads.
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
            } else {
                System.out.println(inputBuffer);
            }


        }
    }

    private void sendToProxy(String reactionFromArduino) {


        try {
            System.out.println("Sending to server: " + huiscentrale.getServercommand() + ";" + huiscentrale.getClient_id() + ";" + huiscentrale.getRequestForId() + ";" + reactionFromArduino);
            huiscentrale.proxy.sendResponse(huiscentrale.getServercommand(), huiscentrale.getClient_id(), huiscentrale.getRequestForId(), reactionFromArduino);
        } catch (Exception e) {
            System.out.println("HC cannot connect server. " + e);
        }
    }
}