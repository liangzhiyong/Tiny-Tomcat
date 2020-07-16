import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author JohnLeung
 * @date 2020/07/15
 */
public class Response {

    private static final int BUFFER_SIZE = 2048;

    private Request request;

    private OutputStream outputStream;

    public Response(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void sendStaticResource() {

        byte[] bytes = new byte[BUFFER_SIZE];
        FileInputStream fis = null;

        try {
            File file = new File(HttpServer.WEB_ROOT, request.getUri());
            if (file.exists()) {
                // 读取文件
                fis = new FileInputStream(file);
                // 从0位置开始，读取 BUFFER_SIZE 个字节到 bytes 里
                int ch = fis.read(bytes, 0, BUFFER_SIZE);
                // 如果没有到文件尾，就放入 outputStream
                while (ch != -1) {
                    outputStream.write(bytes, 0, ch);
                    // 再次读取 fis 到 bytes
                    ch = fis.read(bytes, 0, BUFFER_SIZE);
                }
            } else {
                // 没有该文件
                String errorMessage = "404 File Not Found!";
                outputStream.write(errorMessage.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }


    public void setRequest(Request request) {
        this.request = request;
    }
}
