import java.io.*;
import java.net.*;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int PORT = 1234;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SERVER_ADDRESS, PORT);
            System.out.println("Connected to chat server.");

            new ReadThread(socket).start();
            new WriteThread(socket).start();

        } catch (IOException ex) {
            System.out.println("Error connecting to server: " + ex.getMessage());
        }
    }

    static class ReadThread extends Thread {
        private BufferedReader reader;

        public ReadThread(Socket socket) throws IOException {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }

        public void run() {
            try {
                String message;
                while ((message = reader.readLine()) != null) {
                    System.out.println("\n" + message);
                }
            } catch (IOException e) {
                System.out.println("Connection closed.");
            }
        }
    }

    static class WriteThread extends Thread {
        private PrintWriter writer;
        private BufferedReader consoleInput;

        public WriteThread(Socket socket) throws IOException {
            writer = new PrintWriter(socket.getOutputStream(), true);
            consoleInput = new BufferedReader(new InputStreamReader(System.in));
        }

        public void run() {
            try {
                String text;
                while ((text = consoleInput.readLine()) != null) {
                    writer.println(text);
                    if (text.equalsIgnoreCase("exit")) {
                        System.out.println("Disconnected from chat.");
                        System.exit(0);
                    }
                }
            } catch (IOException e) {
                System.out.println("Error sending message.");
            }
        }
    }
}
