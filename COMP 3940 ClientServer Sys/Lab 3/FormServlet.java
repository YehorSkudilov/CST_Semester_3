import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
public class FormServlet extends HttpServlet {
   @Override
   public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
      response.send("200 OK", "text/html; charset=UTF-8", Files.readAllBytes(Paths.get("Form.html")));
   }
}
