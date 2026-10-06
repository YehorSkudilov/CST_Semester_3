import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;
public class UploadServlet implements HttpServlet {
   private final FileDAO fileDAO = new FileDAO("uploads");

   public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
      response.sendHtml("200 OK", new String(Files.readAllBytes(Paths.get("Form.html")), "UTF-8"));
   }

   public void doPost(HttpServletRequest request, HttpServletResponse response) throws UploadException, IOException {
      String contentType = request.getHeader("content-type");
      String boundary = "--" + contentType.substring(contentType.indexOf("boundary=") + 9);

      String body = new String(request.getInputStream().readAllBytes(), "ISO-8859-1");
      Map<String, String> fields = new HashMap<>();
      String fileName = null;
      String fileData = null;
      for (String part : body.split(java.util.regex.Pattern.quote(boundary))) {
         int headerEnd = part.indexOf("\r\n\r\n");
         if (headerEnd < 0) continue;
         String partHeaders = part.substring(0, headerEnd);
         String content = part.substring(headerEnd + 4, part.length() - 2);
         if (partHeaders.contains("filename=\"")) {
            fileName = between(partHeaders, "filename=\"", "\"");
            fileData = content;
         } else {
            fields.put(between(partHeaders, "name=\"", "\""), content);
         }
      }
      if (fileName == null || fileName.isEmpty()) throw new UploadException(400, "No file selected");

      String savedName = Stream.of(fields.get("caption"), fields.get("date"), fileName)
                               .collect(Collectors.joining("_"));
      fileDAO.save(savedName, fileData.getBytes("ISO-8859-1"));

      String items = fileDAO.listFiles().stream()
                            .map(f -> "<li>" + f + "</li>")
                            .collect(Collectors.joining());
      response.sendHtml("200 OK", "<html><body><ul>" + items + "</ul></body></html>");
   }

   private static String between(String text, String start, String end) {
      int s = text.indexOf(start) + start.length();
      return text.substring(s, text.indexOf(end, s));
   }
}
