// Custom exception: carries the HTTP status code that should be sent back to the browser.
public class UploadException extends Exception {
   private final int statusCode;

   public UploadException(int statusCode, String message) {
      super(message);
      this.statusCode = statusCode;
   }
   public UploadException(int statusCode, String message, Throwable cause) {
      super(message, cause);
      this.statusCode = statusCode;
   }

   public int getStatusCode() {return statusCode;}

   public String getStatusLine() {
      return statusCode + " " + switch (statusCode) {
         case 400 -> "Bad Request";
         case 404 -> "Not Found";
         case 405 -> "Method Not Allowed";
         default -> "Internal Server Error";
      };
   }
}
