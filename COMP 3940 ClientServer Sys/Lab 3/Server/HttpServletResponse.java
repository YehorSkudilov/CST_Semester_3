import java.io.*;
public class HttpServletResponse {
   private OutputStream outputStream = null;
   public HttpServletResponse(OutputStream outputStream) {
      this.outputStream = outputStream;
   }
   public OutputStream getOutputStream() {return outputStream;}

   public void sendHtml(String status, String html) throws IOException {
      byte[] body = html.getBytes("UTF-8");
      String headers = "HTTP/1.1 " + status + "\r\n"
                     + "Content-Type: text/html; charset=UTF-8\r\n"
                     + "Content-Length: " + body.length + "\r\n"
                     + "Connection: close\r\n\r\n";
      outputStream.write(headers.getBytes());
      outputStream.write(body);
      outputStream.flush();
   }
}
