import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class Connection {

    /**
     * Функция считывает ответ с какого-то connection
     * @param connection - HttpsURLConnection который передает считываемый функцией ответ
     * @throws IOException
     */
    public static String readAnswer(HttpsURLConnection connection) throws IOException {
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

    /**
     *
     * @param strURL
     * @return
     */
    static private String getPage(String strURL){
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

}
