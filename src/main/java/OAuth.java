import javax.net.ssl.HttpsURLConnection;
import java.awt.*;
import java.io.IOException;
import java.net.*;
import java.util.List;

public class OAuth {
    String clientId = "629355298063-7roidu7ht5ar5e7q0irn7h52mf1me2ob.apps.googleusercontent.com";
    String clientSecret = "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";
    String endPoint = "https://accounts.google.com/o/oauth2/v2/auth";
    List<String> scope;

    HttpsURLConnection connection;

    URL url;

    OAuth() throws Exception {
        url = new URL(endPoint);
        connection = (HttpsURLConnection) url.openConnection();
        connection.addRequestProperty("client_id",clientId);
//        connection.addRequestProperty("redirect_uri", "com.example.app : redirect_uri_path");
//        connection.addRequestProperty("response_type","code");
//        connection.addRequestProperty("scope","https://www.googleapis.com/auth/spreadsheets");
//        connection.addRequestProperty("code_challenge_method", "sha256");
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)){
            Desktop.getDesktop().browse(new URI(endPoint+"?"+"client_id=629355298063-7roidu7ht5ar5e7q0irn7h52mf1me2ob.apps.googleusercontent.com&" +
                    "redirect_uri=http://localhost:1234&response_type=code&scope=https://www.googleapis.com/auth/spreadsheets"));
        }

    }

    public void exchengeToken(HttpsURLConnection connection, String codeVerifier, String redirectUrl) throws IOException, URISyntaxException {
        String tokenEndPoint = "https://oauth2.googleapis.com/token";
        connection.addRequestProperty("client_id", clientId);
        connection.addRequestProperty("client_secret", clientSecret);
        connection.addRequestProperty("code", "sha256");
        connection.addRequestProperty("code_verifier", codeVerifier);
        connection.addRequestProperty("grant_type", "authorization_code");
        connection.addRequestProperty("redirect_uri", "https://www.youtube.com/watch?v=TEmLkXAiacA");
        connection.setRequestMethod("POST");
        connection.connect();
        Connection.readAnswer(connection);
    }

}
