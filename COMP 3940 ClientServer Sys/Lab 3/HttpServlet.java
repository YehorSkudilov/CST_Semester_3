import java.io.*;
public abstract class HttpServlet implements Servlet {
   public void doGet(HttpServletRequest request, HttpServletResponse response) throws UploadException, IOException {
      throw new UploadException(405, "GET is not supported on " + request.getPath());
   }
   public void doPost(HttpServletRequest request, HttpServletResponse response) throws UploadException, IOException {
      throw new UploadException(405, "POST is not supported on " + request.getPath());
   }
}
