import com.hashira.oauth.OAuth;

public class Main {
    public static void main(String[] args) throws Exception {
        OAuth oAuth = new OAuth("629355298063-7roidu7ht5ar5e7q0irn7h52mf1me2ob.apps.googleusercontent.com",
                "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",
                "https://www.googleapis.com/auth/spreadsheets");
        System.out.println(oAuth.getOauthAccessToken());
    }
}
