import java.io.*;
import java.net.*;
public class UploadClient {
    public UploadClient() { }
    public String uploadFile(String caption, String date, String fileName) {
        String listing = "";
        try {
            FileInputStream fis = new FileInputStream(fileName);
            byte[] bytes = fis.readAllBytes();
            fis.close();

            String boundary = "----UploadClientBoundary" + System.currentTimeMillis();
            ByteArrayOutputStream body = new ByteArrayOutputStream();
            body.write(("--" + boundary + "\r\n"
                + "Content-Disposition: form-data; name=\"caption\"\r\n\r\n"
                + caption + "\r\n"
                + "--" + boundary + "\r\n"
                + "Content-Disposition: form-data; name=\"date\"\r\n\r\n"
                + date + "\r\n"
                + "--" + boundary + "\r\n"
                + "Content-Disposition: form-data; name=\"fileName\"; filename=\"" + new File(fileName).getName() + "\"\r\n"
                + "Content-Type: application/octet-stream\r\n\r\n").getBytes());
            body.write(bytes);
            body.write(("\r\n--" + boundary + "--\r\n").getBytes());

            Socket socket = new Socket("localhost", 8082);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
            OutputStream out = socket.getOutputStream();
            out.write(("POST /upload HTTP/1.1\r\n"
                + "Host: localhost:8082\r\n"
                + "Content-Type: multipart/form-data; boundary=" + boundary + "\r\n"
                + "Content-Length: " + body.size() + "\r\n"
                + "Connection: close\r\n\r\n").getBytes());
            body.writeTo(out);
            out.flush();

            String line = "";
            while ((line = in.readLine()) != null) {
                listing += line + "\n";
            }
            socket.close();
        } catch (Exception e) {
            System.err.println(e);
        }
        return listing;
    }
}
