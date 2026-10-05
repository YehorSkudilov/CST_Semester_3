import java.io.*;
import java.util.function.Function;
import java.util.stream.*;
public class UploadServlet extends HttpServlet {
   private final FileDAO fileDAO = new FileSystemFileDAO("uploads");

   // Functional: small pure functions composed into one filename cleaner
   private static final Function<String, String> OR_NONE = s -> (s == null || s.isBlank()) ? "none" : s;
   private static final Function<String, String> SAFE_CHARS = s -> s.replaceAll("[\\\\/:*?\"<>|&\\s]", "_");
   private static final Function<String, String> CLEAN = OR_NONE.andThen(SAFE_CHARS);

   @Override
   public void doPost(HttpServletRequest request, HttpServletResponse response) throws UploadException, IOException {
      if (request.getBoundary() == null)
         throw new UploadException(400, "Expected a multipart/form-data request");
      String fileName = request.getFileName();
      if (fileName == null || fileName.isEmpty())
         throw new UploadException(400, "No file selected");

      String savedName = Stream.of(request.getParameter("caption"), request.getParameter("date"), fileName)
                               .map(CLEAN)
                               .collect(Collectors.joining("_"));
      byte[] fileData = request.getFileData();
      String path = fileDAO.save(savedName, fileData);
      System.out.println("Saved " + path + " (" + fileData.length + " bytes)");

      response.send("200 OK", "text/html; charset=UTF-8", page("Uploaded " + savedName));
   }

   @Override
   public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
      response.send("200 OK", "text/html; charset=UTF-8", page("Uploaded files"));
   }

   private String page(String heading) throws IOException {
      String items = fileDAO.listFiles().stream()
                            .map(name -> "<li>" + name + "</li>")
                            .collect(Collectors.joining());
      return "<html><body><h2>" + heading + "</h2><ul>" + items + "</ul><a href=\"/\">Upload another</a></body></html>";
   }
}
