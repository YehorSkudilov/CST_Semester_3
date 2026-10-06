import java.io.*;
import java.util.*;
public class Activity {
   public static void main(String[] args) throws IOException {
      new Activity().onCreate();
   }
   public Activity() {
   }
   public void onCreate() {
      Scanner sc = new Scanner(System.in);
      while (true) {
         System.out.print("Enter c to upload a file or q to quit: ");
         String choice = sc.nextLine();
         if (choice.equals("q")) break;
         if (!choice.equals("c")) continue;
         System.out.print("Caption: ");
         String caption = sc.nextLine();
         System.out.print("Date (yyyy-mm-dd): ");
         String date = sc.nextLine();
         System.out.print("File path: ");
         String fileName = sc.nextLine();
         System.out.println(new UploadClient().uploadFile(caption, date, fileName));
      }
   }
}
