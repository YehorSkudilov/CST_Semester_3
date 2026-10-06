public class UploadException extends Exception {
   private final int statusCode;
   public UploadException(int statusCode, String message) {
      super(message);
      this.statusCode = statusCode;
   }
   public int getStatusCode() {return statusCode;}
}
