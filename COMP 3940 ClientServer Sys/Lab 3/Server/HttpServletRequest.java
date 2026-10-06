import java.io.*;
import java.util.*;
public class HttpServletRequest {
   private InputStream inputStream = null;
   private Map<String, String> headers = new HashMap<>();
   public HttpServletRequest(InputStream inputStream, Map<String, String> headers) {
      this.inputStream = inputStream;
      this.headers = headers;
   }
   public InputStream getInputStream() {return inputStream;}
   public String getHeader(String name) {return headers.get(name.toLowerCase());}
}
