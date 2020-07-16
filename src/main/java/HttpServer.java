import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author JohnLeung
 * @date 2020/07/14
 */
public class HttpServer {

    public static final String WEB_ROOT = "/Users/John-MacAir/03Projects/tiny-tomcat/src/main/resources";

    public static final String SHUTDOWN_URL = "/shutdown";

    private boolean shutdown = false;

    public static void main(String[] args) {
        HttpServer httpServer = new HttpServer();
        httpServer.await();
    }


    private void await() {
        ServerSocket serverSocket = null;
        int port = 8080;

        // 创建 ServerSocket
        try {
            serverSocket = new ServerSocket(port, 1,
                    InetAddress.getByName("127.0.0.1"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // 循环等待请求
        while (!shutdown) {
            Socket socket = null;
            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                // 创建 Socket
                socket = serverSocket.accept();
                // 获得 InputStream/OutputStream
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();

                // 创建 Request
                Request request = new Request(inputStream);
                request.parse();

                // 创建 Response
                Response response = new Response(outputStream);
                response.setRequest(request);
                response.sendStaticResource();

                // close socket
                socket.close();

                // 设置服务器连接状态，如果是 shutdown
                shutdown = request.getUri().equalsIgnoreCase(SHUTDOWN_URL);
            } catch (Exception e) {
                e.printStackTrace();
                // 单次请求异常，继续监听请求连接
                continue;
            }
        }

    }
}
