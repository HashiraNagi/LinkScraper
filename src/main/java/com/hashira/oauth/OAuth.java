package com.hashira.oauth;

import javax.net.ssl.HttpsURLConnection;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;


/**
 * Класс, который авторизуется в google, и получает токен для доступа к API
 * (точно работает на десктопе, на других платформах не тестирован)
 * @author hashira
 */
public class OAuth implements Runnable{

    protected static String authorizationCode = null;
    private String codeToTokenExchangeResult;
    private String accessToken = null;
    private int expiresIn;
    private String refreshToken = null;
    private final String clientId;
    private final String clientSecret;
    private final String scope;

    /**
     * Гетер для токена
     * @return токен
     * @throws InterruptedException
     */
    public String getOauthAccessToken() throws InterruptedException {
        while (accessToken==null){
            Thread.sleep(10);
        }
        return accessToken;
    }

    /**
     * Конструктор для OАuth. Прямо тут и запускается поток для получения и подальшего обновления токена
     * @param clientId Клиент айди это поле которое получается при регистрации приложения в google developer console
     * @param clientSecret Клиент сикрет поле которое получается при регистрации приложения в google developer console
     * @param scope - поле указывает к какому конкретно API токен даст доступ, для указания нескольких scope просто пишите их через +
     * @throws Exception
     */
    public OAuth(String clientId, String clientSecret, String scope) throws Exception {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.scope = scope;
        Thread tokenUpdating = new Thread(this);
        tokenUpdating.setDaemon(true);
        tokenUpdating.start();
    }

    /**
     *Метод который получает authorization code
     * @throws IOException
     * @throws URISyntaxException
     */
    private void getCode() throws IOException, URISyntaxException {
        ServerAccept accept = new ServerAccept(); // создаем сервер, который прослушивает порт на который придет authorization code
        Thread acceptServer =new Thread(accept);
        acceptServer.start(); // запускаем прослушку в отдельном потоке

        /*Открываем в браузере по умолчанию страницу авторизации,
        которая после того как пользователь даст нужные разрешения, отправит GET запрос с нужным нам кодом.
        Запрос получит сервер который мы создали выше и положит код в переменную */
        String endPoint = "https://accounts.google.com/o/oauth2/v2/auth";
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)){
            Desktop.getDesktop().browse(new URI(endPoint +"?"+"client_id="+clientId+"&" +
                    "redirect_uri=http://localhost:1234&response_type=code&scope="+scope+""));
        }

    }

    /**
     *Функция обменивает authorization code на страницу содержащую refresh token и остальные данные
     * @throws IOException
     */
    private void exchange() throws IOException {
        URL url1 = new URL("https://oauth2.googleapis.com/token");
        HttpsURLConnection connection1 = (HttpsURLConnection) url1.openConnection();
        connection1.setDoOutput(true);
        connection1.setRequestMethod("POST");
        connection1.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
        OutputStream outputStream = connection1.getOutputStream();
        String req = "code="+authorizationCode+"&" +
                "client_id="+clientId+"&" +
                "client_secret="+clientSecret+"&" +
                "redirect_uri=http://localhost:1234&" +
                "grant_type=authorization_code";
        byte[] bytes =req.getBytes(StandardCharsets.UTF_8);
        outputStream.flush();
        outputStream.write(bytes);
        codeToTokenExchangeResult = readAnswer(connection1);
    }

    /**
     *Функция, которая объединяет функции выше и получает refresh token
     * @throws IOException
     * @throws URISyntaxException
     * @throws InterruptedException
     */
    private void getRefreshToken() throws IOException, URISyntaxException, InterruptedException {
        getCode();
        while (authorizationCode==null){
            Thread.sleep(10);
        }
        exchange();
        String tempAnswer = codeToTokenExchangeResult;
        tempAnswer = tempAnswer.substring(tempAnswer.indexOf("\"refresh_token\": \"")).split("\"")[3];
        refreshToken = tempAnswer;
    }

    /**
     *Функция получает access token отправляя refresh token
     * @throws IOException
     */
    private void getAccessToken() throws IOException {
        URL url1 = new URL("https://oauth2.googleapis.com/token");
        HttpsURLConnection connection = (HttpsURLConnection) url1.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
        OutputStream outputStream = connection.getOutputStream();
        String req = "client_id="+clientId+"&" +
                "client_secret="+clientSecret+"&" +
                "refresh_token="+refreshToken+"&" +
                "grant_type=refresh_token";
        outputStream.flush();
        outputStream.write(req.getBytes(StandardCharsets.UTF_8));
        String temp = readAnswer(connection);
        accessToken = temp.substring(temp.indexOf("\"access_token\": \"")).split("\"")[3];
        expiresIn = Integer.parseInt(temp.substring(temp.indexOf("\"expires_in\": ")).split(":")[1].split(",")[0].trim());
    }

    /**
     *
     * @param connection
     * @throws IOException
     */
    private void readError(HttpsURLConnection connection) throws IOException {
        InputStream inputStream = connection.getErrorStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line = bufferedReader.readLine();
        StringBuilder sb = new StringBuilder();
        while (line != null){
            sb.append(line);
            sb.append("\n");
            line = bufferedReader.readLine();
        }

        System.out.println(sb);
    }

    /**
     * Функция считывает ответ с какого-то connection
     * @param connection - HttpsURLConnection который передает считываемый функцией ответ
     * @throws IOException
     */
    private static String readAnswer(HttpsURLConnection connection) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line = bufferedReader.readLine();
        StringBuilder sb = new StringBuilder();

        while (line!=null){
            sb.append(line);
            sb.append("\n");
            line = bufferedReader.readLine();
        }
        return sb.toString();
    }

    @Override
    public void run() {
        try {
            getRefreshToken();
            getAccessToken();
            while (true) {
                Thread.sleep(expiresIn * 999L);
                getAccessToken();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
