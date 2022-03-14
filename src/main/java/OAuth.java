import javax.net.ssl.HttpsURLConnection;
import java.awt.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class OAuth {

    String codeToTokenExchangeResult;
    String accessToken = null;
    String expiresIn = null;
    String refreshToken = null;
    String clientId;
    String clientSecret;
    String scope;
    String endPoint = "https://accounts.google.com/o/oauth2/v2/auth";

    HttpsURLConnection connection;

    URL url;

    OAuth(String clientId, String clientSecret, String scope) throws Exception {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.scope = scope;
    }

    private void getCode() throws IOException, URISyntaxException {
        ServerAccept accept = new ServerAccept();
        Thread q =new Thread(accept);
        q.start();

        url = new URL(endPoint);
        connection = (HttpsURLConnection) url.openConnection();
        connection.addRequestProperty("client_id",clientId);
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)){
            Desktop.getDesktop().browse(new URI(endPoint+"?"+"client_id="+clientId+"&" +
                    "redirect_uri=http://localhost:1234&response_type=code&scope="+scope+""));
        }
    }

    private void exchange() throws IOException {
        URL url1 = new URL("https://oauth2.googleapis.com/token");
        HttpsURLConnection connection1 = (HttpsURLConnection) url1.openConnection();
        connection1.setDoOutput(true);
        connection1.setRequestMethod("POST");
        connection1.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
        OutputStream outputStream = connection1.getOutputStream();
        String req = "code="+ServerAccept.code+"&" +
                "client_id="+clientId+"&" +
                "client_secret="+clientSecret+"&" +
                "redirect_uri=http://localhost:1234&" +
                "grant_type=authorization_code";
        byte[] bytes =req.getBytes(StandardCharsets.UTF_8);
        outputStream.flush();
        outputStream.write(bytes);
        codeToTokenExchangeResult = Connection.readAnswer(connection1);
    }

    public String getRefreshToken() throws IOException, URISyntaxException, InterruptedException {
        getCode();
        while (ServerAccept.code==null){
            Thread.sleep(10);
        }
        exchange();
        String tempAnswer = codeToTokenExchangeResult;
        System.out.println(tempAnswer);
        tempAnswer = tempAnswer.substring(tempAnswer.indexOf("\"refresh_token\": \"")).split("\"")[3];
        return tempAnswer;
    }

}
