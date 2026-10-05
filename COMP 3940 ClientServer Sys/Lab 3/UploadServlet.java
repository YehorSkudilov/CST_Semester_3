import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
public class UploadServlet extends HttpServlet {
   protected void doPost(HttpServletRequest request, HttpServletResponse response) {
      try {
         String caption = request.getParameter("caption");
         String date = request.getParameter("date");
         String fileName = request.getFileName();
         byte[] fileData = request.getFileData();

         if (fileName == null || fileName.isEmpty()) {
            send(response, "400 Bad Request", "<h2>No file selected</h2>");
            return;
         }

         File dir = new File("uploads");
         dir.mkdirs();
         String savedName = clean(caption) + "_" + clean(date) + "_" + clean(fileName);
         File saved = new File(dir, savedName);
         Files.write(saved.toPath(), fileData);
         System.out.println("Saved " + saved.getPath() + " (" + fileData.length + " bytes)");

         StringBuilder html = new StringBuilder();
         html.append("<h2>Uploaded ").append(savedName).append("</h2><ul>");
         String[] chld = dir.list();
         Arrays.sort(chld);
         for (int i = 0; i < chld.length; i++) {
            html.append("<li>").append(chld[i]).append("</li>");
         }
         html.append("</ul><a href=\"/\">Upload another</a>");
         send(response, "200 OK", html.toString());
      } catch(Exception ex) {
         System.err.println(ex);
      }
   }

   // strip characters that are not allowed in file names
   private static String clean(String s) {
      if (s == null || s.isEmpty()) return "none";
      return s.replaceAll("[\\\\/:*?\"<>|\\s]", "_");
   }

   private static void send(HttpServletResponse response, String status, String body) throws IOException {
      byte[] bytes = ("<html><body>" + body + "</body></html>").getBytes(StandardCharsets.UTF_8);
      OutputStream out = response.getOutputStream();
      String headers =
         "HTTP/1.1 " + status + "\r\n" +
         "Content-Type: text/html; charset=UTF-8\r\n" +
         "Content-Length: " + bytes.length + "\r\n" +
         "Connection: close\r\n" +
         "\r\n";
      out.write(headers.getBytes(StandardCharsets.ISO_8859_1));
      out.write(bytes);
      out.flush();
   }
}
