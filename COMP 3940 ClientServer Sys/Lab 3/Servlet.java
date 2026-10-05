import java.io.*;
// Component contract: every pluggable component loaded by ServletContainer implements this.
// Being an interface also lets LoggingAspect wrap components in a dynamic proxy.
public interface Servlet {
   void doGet(HttpServletRequest request, HttpServletResponse response) throws UploadException, IOException;
   void doPost(HttpServletRequest request, HttpServletResponse response) throws UploadException, IOException;
}
