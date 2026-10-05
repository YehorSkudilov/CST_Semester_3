import java.io.*;
import java.nio.charset.StandardCharsets;
public class HttpServletResponse {
   private OutputStream outputStream = null;
   public HttpServletResponse(OutputStream outputStream) {
      this.outputStream = outputStream;
   }
   public OutputStream getOutputStream() {return outputStream;}

   public void send(String status, String contentType, String body) throws IOException {
      send(status, contentType, body.getBytes(StandardCharsets.UTF_8));
   }

   public void send(String status, String contentType, byte[] body) throws IOException {
      String headers =
         "HTTP/1.1 " + status + "\r\n" +
         "Content-Type: " + contentType + "\r\n" +
         "Content-Length: " + body.length + "\r\n" +
         "Connection: close\r\n" +
         "\r\n";
      outputStream.write(headers.getBytes(StandardCharsets.ISO_8859_1));
      outputStream.write(body);
      outputStream.flush();
   }
}
