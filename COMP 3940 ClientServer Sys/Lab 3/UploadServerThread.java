import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.*;
import java.util.*;
public class UploadServerThread extends Thread {
   private Socket socket = null;
   public UploadServerThread(Socket socket) {
      super("DirServerThread");
      this.socket = socket;
   }
   public void run() {
      try {
         // read straight from the socket stream (no BufferedReader/DataInputStream),
         // otherwise part of the body gets swallowed into their buffer
         InputStream in = socket.getInputStream();
         OutputStream out = socket.getOutputStream();

         String input = readLine(in);
         System.out.println("Input: " + input);
         if (input == null) { socket.close(); return; }

         Map<String, String> headers = new HashMap<>();
         String line;
         while ((line = readLine(in)) != null && !line.isEmpty()) {
            int colon = line.indexOf(':');
            if (colon > 0)
               headers.put(line.substring(0, colon).trim().toLowerCase(), line.substring(colon + 1).trim());
         }

         if (input.startsWith("GET / ")) {
            byte[] html = Files.readAllBytes(Paths.get("Form.html"));
            send(out, "200 OK", "text/html", html);
         } else if (input.startsWith("POST ")) {
            int length = Integer.parseInt(headers.getOrDefault("content-length", "0"));
            byte[] body = in.readNBytes(length);
            HttpServletRequest req = new HttpServletRequest(new ByteArrayInputStream(body), headers);
            HttpServletResponse res = new HttpServletResponse(out);
            new UploadServlet().doPost(req, res);
         } else if (input.startsWith("GET ")) {
            send(out, "404 Not Found", "text/plain", "Not Found".getBytes());
         } else {
            send(out, "400 Bad Request", "text/plain", "Bad Request".getBytes());
         }

         socket.close();
      } catch (Exception e) { e.printStackTrace(); }
   }

   private static String readLine(InputStream in) throws IOException {
      ByteArrayOutputStream line = new ByteArrayOutputStream();
      int b;
      while ((b = in.read()) != -1) {
         if (b == '\n') break;
         if (b != '\r') line.write(b);
      }
      if (b == -1 && line.size() == 0) return null;
      return line.toString(StandardCharsets.UTF_8);
   }

   private static void send(OutputStream out, String status, String contentType, byte[] body) throws IOException {
      String headers =
         "HTTP/1.1 " + status + "\r\n" +
         "Content-Type: " + contentType + "\r\n" +
         "Content-Length: " + body.length + "\r\n" +
         "Connection: close\r\n" +
         "\r\n";
      out.write(headers.getBytes(StandardCharsets.ISO_8859_1));
      out.write(body);
      out.flush();
   }
}
