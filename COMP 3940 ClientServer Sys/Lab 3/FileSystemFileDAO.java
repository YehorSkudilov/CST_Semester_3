import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.stream.*;
// DAO implementation that stores uploads in a folder on the local file system.
public class FileSystemFileDAO implements FileDAO {
   private final Path dir;

   public FileSystemFileDAO(String dir) {
      this.dir = Paths.get(dir);
   }

   public String save(String fileName, byte[] data) throws IOException {
      Files.createDirectories(dir);
      Path target = dir.resolve(fileName);
      Files.write(target, data);
      return target.toString();
   }

   public List<String> listFiles() throws IOException {
      if (!Files.isDirectory(dir)) return List.of();
      try (Stream<Path> files = Files.list(dir)) {
         return files.filter(Files::isRegularFile)
                     .map(p -> p.getFileName().toString())
                     .sorted()
                     .collect(Collectors.toList());
      }
   }
}
