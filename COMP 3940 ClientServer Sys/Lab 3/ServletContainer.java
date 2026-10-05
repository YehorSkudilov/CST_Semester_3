import java.io.*;
import java.util.*;
import java.util.concurrent.*;
// Component-based architecture using reflection: URL paths are mapped to component class names in
// servlets.properties, and the classes are loaded at runtime with Class.forName, so new components
// can be plugged in by editing the config file instead of the server code.
//
// Singleton (creational): one container per server, shared by all pool threads.
public class ServletContainer {
   private static final ServletContainer INSTANCE = new ServletContainer();

   private final Properties routes = new Properties();
   private final Map<String, Servlet> components = new ConcurrentHashMap<>();

   private ServletContainer() {
      try (InputStream in = new FileInputStream("servlets.properties")) {
         routes.load(in);
      } catch (IOException e) {
         System.err.println("Could not load servlets.properties: " + e);
      }
   }

   public static ServletContainer getInstance() {return INSTANCE;}

   public Servlet getServlet(String path) throws UploadException {
      String className = routes.getProperty(path);
      if (className == null) throw new UploadException(404, "No component is mapped to " + path);
      try {
         return components.computeIfAbsent(className, this::createComponent);
      } catch (IllegalStateException e) {
         throw new UploadException(500, e.getMessage(), e.getCause());
      }
   }

   // factory method: builds the component by reflection and wraps it in the logging aspect
   private Servlet createComponent(String className) {
      try {
         Class<?> cls = Class.forName(className);
         if (!Servlet.class.isAssignableFrom(cls))
            throw new IllegalStateException(className + " does not implement Servlet");
         Servlet servlet = (Servlet) cls.getDeclaredConstructor().newInstance();
         System.out.println("Loaded component " + className);
         return LoggingAspect.wrap(servlet);
      } catch (ReflectiveOperationException e) {
         throw new IllegalStateException("Could not load component " + className, e);
      }
   }
}
