import java.io.*;
import java.util.List;
// DAO (structural): servlets talk to this interface and never touch the storage directly,
// so the storage could be swapped (database, cloud bucket...) without changing UploadServlet.
public interface FileDAO {
   String save(String fileName, byte[] data) throws IOException;
   List<String> listFiles() throws IOException;
}
