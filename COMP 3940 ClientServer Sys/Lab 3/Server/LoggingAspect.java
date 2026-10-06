import java.lang.reflect.*;
public class LoggingAspect implements InvocationHandler {
   private final Object target;
   private LoggingAspect(Object target) {
      this.target = target;
   }

   public static HttpServlet wrap(HttpServlet target) {
      return (HttpServlet) Proxy.newProxyInstance(HttpServlet.class.getClassLoader(),
            new Class<?>[] { HttpServlet.class }, new LoggingAspect(target));
   }

   public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      String call = target.getClass().getSimpleName() + "." + method.getName();
      System.out.println("before " + call);
      try {
         return method.invoke(target, args);
      } catch (InvocationTargetException e) {
         throw e.getCause();
      }
   }
}
