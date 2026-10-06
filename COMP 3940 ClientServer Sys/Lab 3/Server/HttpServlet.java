import java.io.*;
public interface HttpServlet {
   void doGet(HttpServletRequest request, HttpServletResponse response) throws UploadException, IOException;
   void doPost(HttpServletRequest request, HttpServletResponse response) throws UploadException, IOException;
}
