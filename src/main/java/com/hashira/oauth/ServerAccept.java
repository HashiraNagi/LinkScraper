package com.hashira.oauth;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 *Сервер на который придет authorization code
 */
public class ServerAccept implements Runnable {

    protected ServerAccept(){

    }

    /**
     *Метод, который прослушивает заданный порт
     * @throws IOException
     */
    protected void startListening() throws IOException {

        ServerSocket serverSocket = new ServerSocket(1234);

            Socket clientSocket = serverSocket.accept();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String line = bufferedReader.readLine();
            StringBuilder sb = new StringBuilder();
            while (line != "") {
                if (line.isEmpty()) {
                    break;
                }
                sb.append(line);
                sb.append("\n");
                line = bufferedReader.readLine();
            }
            OutputStream out = clientSocket.getOutputStream();

            String html = "<html><head><title>simpleTest</title></head><body><h2>You can close this page. Script start working</h2></body></html>";

            String response =
                    "HTTP/1.0 200 OK\r\n" + "Content-Length: "
                            + html.getBytes().length + "\r\n" + "\r\n" + html + "\r\n" + "\r\n";

            out.write(response.getBytes(StandardCharsets.UTF_8));

            String temp = sb.toString();
            temp = temp.substring(temp.indexOf("?code=") + 6);
            temp = temp.split("&")[0];

            OAuth.authorizationCode = temp;

            out.close();
            bufferedReader.close();
            clientSocket.close();
            serverSocket.close();

    }

    @Override
    public void run() {
        try {
            startListening();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
