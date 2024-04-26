import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ConcurrentHashMap;

public class Client extends Thread {
    private BufferedReader input;
    private PrintWriter output;
    private Map<String, Integer> eventTickets = new ConcurrentHashMap<>();
    private ExecutorService threadPool;
    private Socket socket;

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Enter the server hostname:");
            String host = scanner.nextLine();
            System.out.println("Enter the server port:");
            int port = scanner.nextInt();
            new Client(host, port).start();
        } catch (IOException e) {
            System.err.println("Failed to initialize client: " + e.getMessage());
        }
    }

    public Client(String hostname, int port) throws IOException {
        try {
            socket = new Socket(hostname, port);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
            threadPool = Executors.newFixedThreadPool(5); // Configure based on expected concurrency
        } catch (IOException e) {
            System.err.println("Could not connect to server: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (input.ready()) {
                    String message = input.readLine();
                    threadPool.submit(() -> processServerMessage(message));
                }
            }
        } catch (IOException e) {
            System.err.println("Error in communication: " + e.getMessage());
        } finally {
            try {
                socket.close();
                threadPool.shutdown();
            } catch (IOException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    private void processServerMessage(String message) {
        if (message == null) return; // Handle server disconnection or error

        String[] parts = message.split(",");
        switch (parts[0]) {
            case "UPDATE":
                updateTickets(parts[1], Integer.parseInt(parts[2]));
                break;
            case "CONFIRMATION":
                System.out.println("Booking confirmed for " + parts[1] + ". Remaining tickets: " + parts[2]);
                break;
            case "ERROR":
                System.out.println("Error: " + parts[1]);
                break;
            default:
                break;
        }
    }

    private void updateTickets(String event, int remainingTickets) {
        eventTickets.put(event, remainingTickets);
        System.out.println("Updated tickets for " + event + ": " + remainingTickets);
    }
}
