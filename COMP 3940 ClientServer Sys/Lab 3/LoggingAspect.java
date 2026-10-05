import java.lang.reflect.*;
// AOP: logging and timing are a cross-cutting concern, so instead of writing them inside every
// servlet they are woven around each component call by a dynamic proxy (the "advice").
public class LoggingAspect implements InvocationHandler {
   private final Object target;

   private LoggingAspect(Object target) {
      this.target = target;
   }

   public static Servlet wrap(Servlet target) {
      return (Servlet) Proxy.newProxyInstance(
         Servlet.class.getClassLoader(),
         new Class<?>[] { Servlet.class },
         new LoggingAspect(target));
   }

   public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      String call = target.getClass().getSimpleName() + "." + method.getName();
      String thread = Thread.currentThread().getName();
      long start = System.nanoTime();
      System.out.println("[" + thread + "] before " + call);
      try {
         Object result = method.invoke(target, args);
         System.out.println("[" + thread + "] after  " + call + " (" + (System.nanoTime() - start) / 1_000_000 + " ms)");
         return result;
      } catch (InvocationTargetException e) {
         // rethrow the real exception so callers still see UploadException / IOException
         System.out.println("[" + thread + "] " + call + " threw " + e.getCause());
         throw e.getCause();
      }
   }
}
