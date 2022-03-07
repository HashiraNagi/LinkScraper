import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.List;

public class Connection {

    static String token = null;


    String APIKey = "AIzaSyDroQ9YbxQQPhHzPibbL1mPovbGPuiMMxM";

    public static void readAnswer(HttpsURLConnection connection) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line = bufferedReader.readLine();
        StringBuilder sb = new StringBuilder();

        while (line!=null){
            sb.append(line);
            sb.append("\n");
            line = bufferedReader.readLine();
        }
        System.out.println(sb);
    }


    private String getPage(String strURL){
        String res = null;
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = bufferedReader.readLine();
            StringBuilder sb = new StringBuilder();

            while (line!=null){
                sb.append(line);
                sb.append("\n");
                line = bufferedReader.readLine();
            }
            res = sb.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }

    public List<String> getLinks(String strURL, String comp){
        String temp = comp;
        comp = "https://www." + comp;
        String str = getPage(strURL);
        String link;
        List<String> res = new ArrayList<>();
        while (str.contains(comp)) {
            str = str.substring(str.indexOf(comp));
            link = str.split("\"")[0];
            str = str.split("\"")[str.split("\"").length-1];
            res.add(link);
        }
        //----
        comp = temp;
        comp = "https://" + comp;
        str = getPage(strURL);
        while (str.contains(comp)) {
            str = str.substring(str.indexOf(comp));
            link = str.split("\"")[0];
            str = str.split("\"")[str.split("\"").length-1];
            res.add(link);
        }
        return res;
    }


    public void exchangeCodeToToken(){

    }










    public void write(String str){
        HttpsURLConnection connection = null;
        try {
            URL url = new URL("https://sheets.googleapis.com/v4/spreadsheets/16lk4ap51KWB13eaYiSW1gXh4o6Mc62Y5lhTG8IS9jHY/values/c1?key=AIzaSyDroQ9YbxQQPhHzPibbL1mPovbGPuiMMxM");
            connection = (HttpsURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            HttpTransport qw = new ApacheHttpTransport();
//            GoogleAuthorizationCodeFlow authorizationCodeFlow = new GoogleAuthorizationCodeFlow(new ApacheHttpTransport(),
//                    new JacksonFactory(),"629355298063-7roidu7ht5ar5e7q0irn7h52mf1me2ob.apps.googleusercontent.com",
//                    "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",new ArrayList<String>());


//            GoogleAuthorizationCodeRequestUrl googleAuthorization = new GoogleAuthorizationCodeRequestUrl("629355298063-7roidu7ht5ar5e7q0irn7h52mf1me2ob.apps.googleusercontent.com",
//                    "https://www.youtube.com",new ArrayList<String>());

            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.writeBytes("value = qweewq");
            out.flush();


            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = bufferedReader.readLine();
            StringBuilder sb = new StringBuilder();

            while (line!=null){
                sb.append(line);
                sb.append("\n");
                line = bufferedReader.readLine();
            }
            System.out.println(sb);
        }catch (Exception e){
            e.printStackTrace();

        }

    }
}
