public class ArduinoIn implements Runnable{ // implements Runnable to work with threads.
    private ComPort comPort;

    // constructor that gives this class the same instance of comport as all other serialport communicating classes.
    public ArduinoIn(ComPort comport) {
        this.comPort = comport;
    }

    // the run method is used for multithreading, if this thread starts it kicks off run.
    @Override
    public void run() {
        // As long as the thread is alive this while loop will be alive and will keep listening to what the Arduino has to say.
        while (!Thread.interrupted()) {
            System.out.println(comPort.readInput());
        }
    }
}
