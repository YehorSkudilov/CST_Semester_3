import java.net.*;
import java.io.*;
import java.util.concurrent.*;
public class UploadServer {
    private static final int PORT = 8082;
    private static final int POOL_SIZE = 10;

    public static void main(String[] args) {
        // Thread pool: a fixed set of worker threads is reused for every connection
        // instead of creating a new Thread per request
        ExecutorService pool = Executors.newFixedThreadPool(POOL_SIZE);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Listening on port " + PORT + " with " + POOL_SIZE + " worker threads");
            while (true) {
                pool.execute(new UploadServerThread(serverSocket.accept()));
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + PORT);
        } finally {
            pool.shutdown();
        }
    }
}
