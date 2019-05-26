import com.fazecast.jSerialComm.SerialPort;

import java.io.*;

class ComPort {
    private PrintStream writer;
    private BufferedReader input;
    


    void initialize() {
        // Create a serialport for your pc, check to see what serialport your Arduino is connected to.
        SerialPort comPort = SerialPort.getCommPort("COM3"); // hardcoded comport

        try {
            // open the serialport, serialport can only be opened if there is no other program using it like the Arduino IDE.
            comPort.openPort();
            // set baudrate to the same as Arduino is set, otherwise messages will be garbled
            comPort.setBaudRate(57600);
            comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
            // Create an outputstream.
            OutputStream output = comPort.getOutputStream();
            writer = new PrintStream(output);
            // Create an inputstream.
            input = new BufferedReader(new InputStreamReader(comPort.getInputStream()));
            System.out.println("ComPort succesfully opened");

        } catch (Exception e) {
            System.out.println("ComPort not available");
            // TODO: message to proxy if comport not available
            System.exit(1); // Program ends.
        }
    }

    //method to write output to the Arduino
    void writeOutput(String command) {
        try {
            writer.print(command);
        } catch (Exception e) {
            System.out.println("output writing failed" + e);
        }
    }


    // Method to tell us what the Arduino is telling us.
    String readInput() {
        try {
            return input.readLine();
        } catch (IOException e) {
            return e.toString();
        }
    }


}