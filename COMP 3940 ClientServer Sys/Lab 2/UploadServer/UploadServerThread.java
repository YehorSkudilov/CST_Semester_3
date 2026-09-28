import java.net.*;
import java.io.*;
import java.time.Clock;
import java.util.concurrent.Semaphore;                                   // NEW

public class UploadServerThread extends Thread {
   // NEW: declared and created once, shared by every connection thread
   private static final Semaphore uploadSemaphore = new Semaphore(3, true);

   private Socket socket = null;
   public UploadServerThread(Socket socket) {
      super("DirServerThread");
      this.socket = socket;
   }
   public void run() {
      try {
         InputStream in = socket.getInputStream();
         HttpServletRequest req = new HttpServletRequest(in);
         OutputStream baos = new ByteArrayOutputStream();
         HttpServletResponse res = new HttpServletResponse(baos);
         HttpServlet httpServlet = new UploadServlet();

         uploadSemaphore.acquire();                                      // NEW: blocks if 3 uploads are already running
         try {
            httpServlet.doPost(req, res);                                // existing: the actual upload
         } finally {
            uploadSemaphore.release();                                   // NEW: always frees the permit
         }

         OutputStream out = socket.getOutputStream();
         out.write(((ByteArrayOutputStream) baos).toByteArray());
         socket.close();
      } catch (Exception e) { e.printStackTrace(); }
   }
}
