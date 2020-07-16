import java.io.IOException;
import java.io.InputStream;

/**
 * Request 类
 *
 * @author JohnLeung
 * @date 2020/07/14
 */
public class Request {
    /**
     * 输入流
     */
    private InputStream inputStream;

    /**
     * 请求的 uri
     */
    private String uri;

    public Request(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    /**
     * 解析请求报文，并提取 uri
     */
    public void parse() {
        // 从 socket 中获取字节
        StringBuffer requestBuffer = new StringBuffer(2048);

        int i;
        byte[] buffer = new byte[2048];

        try {
            i = inputStream.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
            i = -1;
        }

        // 输出请求字节
        for (int j = 0; j < i; j++) {
            requestBuffer.append((char) buffer[j]);
        }
        System.out.println("request: " + requestBuffer);

        this.uri = parseUri(requestBuffer.toString());
        System.out.println("uri: " + uri);
    }


    /**
     * 从请求报文中，解析出请求的 uri (获取第一个空格和第二个空格之间的字段即为 uri)
     * @param requestString
     * @return
     */
    private String parseUri(String requestString) {
        int index1, index2;
        // 获取第一个空格的位置
        index1 = requestString.indexOf(" ");
        // 如果第一个空格不是在最后
        if (index1 != -1) {
            // 获取从第一个空格开始的第一个空格位置
            index2 = requestString.indexOf(" ", index1 + 1);
            // 如果 第二个空格位置大于第一个，则输出两空格之间的字符串
            if (index2 > index1) {
                return requestString.substring(index1 + 1, index2);
            }
        }
        return null;
    }

    public String getUri() {
        return uri;
    }

}
