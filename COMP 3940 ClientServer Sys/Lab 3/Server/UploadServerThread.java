import java.net.*;
import java.io.*;
import java.util.*;
public class UploadServerThread implements Runnable {
   private Socket socket = null;
   public UploadServerThread(Socket socket) {
      this.socket = socket;
   }
   public void run() {
      try {
         InputStream in = new BufferedInputStream(socket.getInputStream());
         HttpServletResponse res = new HttpServletResponse(socket.getOutputStream());
         try {
            String[] firstLine = readLine(in).split(" ");
            if (firstLine.length < 2) { // empty/idle connection (e.g. browser preconnect)
               socket.close();
               return;
            }
            String method = firstLine[0];
            String path = firstLine[1];

            Map<String, String> headers = new HashMap<>();
            String line;
            while (!(line = readLine(in)).isEmpty()) {
               int colon = line.indexOf(':');
               headers.put(line.substring(0, colon).trim().toLowerCase(), line.substring(colon + 1).trim());
            }

            int length = Integer.parseInt(headers.getOrDefault("content-length", "0"));
            byte[] body = in.readNBytes(length);
            HttpServletRequest req = new HttpServletRequest(new ByteArrayInputStream(body), headers);

            HttpServlet httpServlet = ServletContainer.getInstance().getServlet(path);
            if (method.equals("GET")) httpServlet.doGet(req, res);
            else if (method.equals("POST")) httpServlet.doPost(req, res);
         } catch (UploadException e) {
            res.sendHtml(e.getStatusCode() + " Error", "<html><body>" + e.getMessage() + "</body></html>");
         }
         socket.close();
      } catch (Exception e) { e.printStackTrace(); }
   }

   private static String readLine(InputStream in) throws IOException {
      ByteArrayOutputStream line = new ByteArrayOutputStream();
      int b;
      while ((b = in.read()) != -1 && b != '\n') {
         if (b != '\r') line.write(b);
      }
      return line.toString("UTF-8");
   }
}
