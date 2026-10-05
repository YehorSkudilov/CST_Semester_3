import java.net.*;
import java.io.*;
public class TcpServer {
    public static void main(String[] args) throws IOException {
        try ( 
           ServerSocket serverSocket = new ServerSocket(8888);
           Socket clientSocket = serverSocket.accept();
           PrintWriter out =
                new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
        ) {
        
            String inputLine, outputLine;
            
            // Initiate conversation with client
            
                System.out.println("before input line");
            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
                //after reading the very first line of the HTTP Request, Respond with an HTTP Response
                out.println("HTTP/1.1 200 OK\r\nContent-length: 11\r\nContent-type: text/html\r\n\r\nHello World");
//                break;     
            }
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port 8889"
                 + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
}
