import com.fazecast.jSerialComm.SerialPort;

import java.io.*;

public class ComPort {
    private SerialPort comPort;
    private OutputStream output; // Where to write to
    private PrintStream writer;
    private BufferedReader input;

    public void initialize() {
        // Create a serialport for your pc, check to see what serialport your Arduino is connected to.
        comPort = SerialPort.getCommPort("COM3"); // hardcoded comport

        try {
            // open the serialport, serialport can only be opened if there is no other program using it like the Arduino IDE.
            comPort.openPort();
            comPort.setBaudRate(57600);
            comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING,0,0);
            // Create an outputstream.
            output = comPort.getOutputStream();
            writer = new PrintStream(output);
            // Create an inputstream.
            input = new BufferedReader(new InputStreamReader(comPort.getInputStream()));

        } catch (Exception e) {
            System.out.println("Comport niet beschikbaar");
            // TODO: message to proxy if comport not available

            System.exit(1); // Bye bye.
        }
    }

    public void writeOutput(String command) {
        try {
            writer.print(command);
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }


    // Method to tell us what the Arduino is telling us.
    public String readInput() {
        try {
            return input.readLine();
        }
        catch (IOException e){
            return e.toString();
        }
    }
//    // Clean up, if we stop the program we need to close the serialport.
//    public synchronized void close() {
//        if (comPort != null) {
//            comPort.closePort();
//        }
//    }
}