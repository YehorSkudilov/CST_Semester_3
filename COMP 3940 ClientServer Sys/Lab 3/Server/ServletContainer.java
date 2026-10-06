import java.io.*;
import java.util.*;
public class ServletContainer {
   private static final ServletContainer instance = new ServletContainer();
   private final Properties routes = new Properties();

   private ServletContainer() {
      try (FileInputStream in = new FileInputStream("servlets.properties")) {
         routes.load(in);
      } catch (IOException e) {
         System.err.println("Could not load servlets.properties: " + e);
      }
   }

   public static ServletContainer getInstance() {return instance;}

   public HttpServlet getServlet(String path) throws UploadException {
      String className = routes.getProperty(path);
      if (className == null) throw new UploadException(404, "Nothing at " + path);
      try {
         HttpServlet servlet = (HttpServlet) Class.forName(className).getDeclaredConstructor().newInstance();
         return LoggingAspect.wrap(servlet);
      } catch (ReflectiveOperationException e) {
         throw new UploadException(500, "Could not load " + className);
      }
   }
}
