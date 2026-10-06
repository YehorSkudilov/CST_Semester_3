import java.io.*;
import java.util.*;
import java.util.stream.*;
// DAO (structural): UploadServlet saves and lists files through this class
// instead of touching the file system directly.
public class FileDAO {
   private final File dir;
   public FileDAO(String dir) {
      this.dir = new File(dir);
   }

   public void save(String fileName, byte[] data) throws IOException {
      dir.mkdirs();
      try (FileOutputStream out = new FileOutputStream(new File(dir, fileName))) {
         out.write(data);
      }
   }

   public List<String> listFiles() {
      String[] names = dir.list();
      if (names == null) return List.of();
      return Arrays.stream(names).sorted().collect(Collectors.toList());
   }
}
