import java.net.ServerSocket;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws Exception {
//        https://docs.google.com/spreadsheets/d/16lk4ap51KWB13eaYiSW1gXh4o6Mc62Y5lhTG8IS9jHY/edit?usp=sharing
//        Connection.getPage("https://docs.oracle.com/javase/tutorial/networking/urls/connecting.html");
        ServerAccept accept = new ServerAccept();
        new Thread(accept).start();
        OAuth oAuth = new OAuth();
//        Connection connection = new Connection();
//        System.out.println(connection.getLinks("https://www.youtube.com/watch?v=9pS6dEt1XXw","instagram.com"));
//        connection.write("q");
    }
}
