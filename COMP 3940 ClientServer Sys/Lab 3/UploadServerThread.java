import java.net.*;
import java.nio.charset.StandardCharsets;
import java.io.*;
import java.util.*;
// Task run by a worker thread from the pool in UploadServer (one per connection).
public class UploadServerThread implements Runnable {
   private Socket socket = null;
   public UploadServerThread(Socket socket) {
      this.socket = socket;
   }
   public void run() {
      try (Socket s = socket) {
         // read straight from the socket stream (no BufferedReader/DataInputStream),
         // otherwise part of the body gets swallowed into their buffer
         InputStream in = s.getInputStream();
         HttpServletResponse res = new HttpServletResponse(s.getOutputStream());
         try {
            handle(in, res);
         } catch (UploadException e) {
            System.err.println(e.getStatusLine() + ": " + e.getMessage());
            String message = e.getMessage().replace("<", "&lt;").replace(">", "&gt;");
            res.send(e.getStatusLine(), "text/html; charset=UTF-8",
               "<html><body><h2>" + e.getStatusLine() + "</h2><p>" + message + "</p></body></html>");
         }
      } catch (IOException e) { e.printStackTrace(); }
   }

   private void handle(InputStream in, HttpServletResponse res) throws IOException, UploadException {
      String input = readLine(in);
      System.out.println("Input: " + input);
      if (input == null) return;

      String[] requestLine = input.split(" ");
      if (requestLine.length < 3) throw new UploadException(400, "Malformed request line");
      String method = requestLine[0];
      String path = requestLine[1].split("\\?")[0];

      Map<String, String> headers = new HashMap<>();
      String line;
      while ((line = readLine(in)) != null && !line.isEmpty()) {
         int colon = line.indexOf(':');
         if (colon > 0)
            headers.put(line.substring(0, colon).trim().toLowerCase(), line.substring(colon + 1).trim());
      }

      int length;
      try {
         length = Integer.parseInt(headers.getOrDefault("content-length", "0"));
      } catch (NumberFormatException e) {
         throw new UploadException(400, "Invalid Content-Length", e);
      }
      byte[] body = in.readNBytes(length);
      HttpServletRequest req = new HttpServletRequest(new ByteArrayInputStream(body), headers, method, path);

      Servlet servlet = ServletContainer.getInstance().getServlet(path);
      switch (method) {
         case "GET" -> servlet.doGet(req, res);
         case "POST" -> servlet.doPost(req, res);
         default -> throw new UploadException(405, method + " is not supported");
      }
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
}
