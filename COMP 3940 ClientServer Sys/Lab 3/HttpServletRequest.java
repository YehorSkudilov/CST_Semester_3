import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.*;

public class HttpServletRequest {
   private InputStream inputStream = null;
   private Map<String, String> headers = new HashMap<>();
   private Map<String, String> parameters = new HashMap<>();
   private String fileName = null;
   private byte[] fileData = null;
   private boolean parsed = false;
   private String method = null;
   private String path = null;

   public HttpServletRequest(InputStream inputStream) {
      this.inputStream = inputStream;
   }
   public HttpServletRequest(InputStream inputStream, Map<String, String> headers) {
      this.inputStream = inputStream;
      this.headers = headers;
   }
   public HttpServletRequest(InputStream inputStream, Map<String, String> headers, String method, String path) {
      this(inputStream, headers);
      this.method = method;
      this.path = path;
   }
   public String getMethod() {return method;}
   public String getPath() {return path;}
   public InputStream getInputStream() {return inputStream;}

   // header names are stored lower-case
   public String getHeader(String name) {return headers.get(name.toLowerCase());}

   // Content-Type: multipart/form-data; boundary=----WebKitFormBoundaryXYZ
   public String getBoundary() {
      String contentType = getHeader("content-type");
      if (contentType == null) return null;
      int idx = contentType.indexOf("boundary=");
      if (idx < 0) return null;
      String boundary = contentType.substring(idx + "boundary=".length()).trim();
      if (boundary.startsWith("\"") && boundary.endsWith("\""))
         boundary = boundary.substring(1, boundary.length() - 1);
      return boundary;
   }

   public String getParameter(String name) throws IOException {
      parseMultipart();
      return parameters.get(name);
   }
   public String getFileName() throws IOException {
      parseMultipart();
      return fileName;
   }
   public byte[] getFileData() throws IOException {
      parseMultipart();
      return fileData;
   }

   // Body layout:
   //   --boundary\r\n
   //   Content-Disposition: form-data; name="caption"\r\n
   //   \r\n
   //   <value>\r\n
   //   --boundary\r\n
   //   Content-Disposition: form-data; name="fileName"; filename="a.png"\r\n
   //   Content-Type: image/png\r\n
   //   \r\n
   //   <raw bytes>\r\n
   //   --boundary--\r\n
   // Everything is handled as bytes so binary files are not corrupted.
   private void parseMultipart() throws IOException {
      if (parsed) return;
      parsed = true;
      String boundary = getBoundary();
      if (boundary == null) return;

      byte[] body = inputStream.readAllBytes();
      byte[] delim = ("--" + boundary).getBytes(StandardCharsets.ISO_8859_1);
      byte[] headerEndMarker = "\r\n\r\n".getBytes(StandardCharsets.ISO_8859_1);

      int pos = indexOf(body, delim, 0);
      while (pos >= 0) {
         int start = pos + delim.length;
         // "--boundary--" marks the end of the body
         if (start + 1 < body.length && body[start] == '-' && body[start + 1] == '-') break;
         start += 2; // skip CRLF after the delimiter

         int next = indexOf(body, delim, start);
         if (next < 0) break;
         int end = next - 2; // drop CRLF before the next delimiter

         int headerEnd = indexOf(body, headerEndMarker, start);
         if (headerEnd >= 0 && headerEnd <= end) {
            String partHeaders = new String(body, start, headerEnd - start, StandardCharsets.UTF_8);
            byte[] content = Arrays.copyOfRange(body, headerEnd + headerEndMarker.length, end);
            String name = extract(partHeaders, "[; ]name=\"([^\"]*)\"");
            String filename = extract(partHeaders, "filename=\"([^\"]*)\"");
            if (filename != null) {
               // some browsers send the full client path
               fileName = filename.substring(Math.max(filename.lastIndexOf('/'), filename.lastIndexOf('\\')) + 1);
               fileData = content;
            } else if (name != null) {
               parameters.put(name, new String(content, StandardCharsets.UTF_8));
            }
         }
         pos = next;
      }
   }

   private static String extract(String text, String regex) {
      Matcher m = Pattern.compile(regex).matcher(text);
      return m.find() ? m.group(1) : null;
   }

   private static int indexOf(byte[] data, byte[] pattern, int from) {
      outer:
      for (int i = from; i <= data.length - pattern.length; i++) {
         for (int j = 0; j < pattern.length; j++) {
            if (data[i + j] != pattern[j]) continue outer;
         }
         return i;
      }
      return -1;
   }
}
