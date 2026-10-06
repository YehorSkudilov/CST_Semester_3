import java.net.*;
import java.io.*;
import java.util.concurrent.*;
public class UploadServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(8082);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 8082.");
            System.exit(-1);
        }
        ExecutorService pool = Executors.newFixedThreadPool(10);
        System.out.println("Listening on http://localhost:8082/");
        while (true) {
            pool.execute(new UploadServerThread(serverSocket.accept()));
        }
    }
}
