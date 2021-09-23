package com.sunquakes.resizer.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ResourceWebServer {
    private static final int SERVER_PORT = 3200;
    private static final int MAX_CONNECTION_LENGTH = 1;

//    public static void main(String[] args) throws IOException {
//        log("======服务器启动=====");
//        ResourceWebServer server = new ResourceWebServer();
//        server.startServer();
//    }

    public void startServer() throws IOException {
        ServerSocket serverSocket = new ServerSocket(SERVER_PORT, MAX_CONNECTION_LENGTH, InetAddress.getByName("localhost"));

        log("======准备接收请求=====");
        while (true) {
            Socket socket = serverSocket.accept();
            try (InputStream inputStream = socket.getInputStream();
                 OutputStream outputStream = socket.getOutputStream()) {

                String requestUri = getRequestUri(inputStream);
                log("请求文件：" + requestUri);

                writeHeaders(outputStream);

                Path path = Paths.get(getClass().getClassLoader().getResource(requestUri.substring(1)).toURI());
                System.out.println(path);
                Files.copy(path, outputStream);
            } catch (Exception e) {
                log("发生错误啦！");
                e.printStackTrace();
            }
        }
    }

    private void writeHeaders(OutputStream outputStream) throws IOException {
        //必须包含返回头，否则浏览器不识别
        outputStream.write("HTTP/1.1 200 OK\r\n".getBytes());
        //一个\r\n代表换行添加新的头，2次\r\n代表头结束
        outputStream.write("Content-Type: text/html\r\n\r\n".getBytes());
    }

    private String getRequestUri(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder(2048);
        byte[] buffer = new byte[2048];
        int size = inputStream.read(buffer);

        for (int i = 0; i < size; i++) {
            stringBuilder.append((char) buffer[i]);
        }

        String requestUri = stringBuilder.toString();
        //此时的uri还包含了请求头等信息，需要去掉
        //GET /index.html HTTP/1.1...
        int index1, index2;
        index1 = requestUri.indexOf(" ");
        if (index1 != -1) {
            index2 = requestUri.indexOf(" ", index1 + 1);
            if (index2 > index1) {
                return requestUri.substring(index1 + 1, index2);
            }
        }
        return "";
    }

    private static void log(Object object) {
        System.out.println(object);
    }
}
