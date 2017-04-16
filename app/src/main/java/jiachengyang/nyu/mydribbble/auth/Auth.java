package jiachengyang.nyu.mydribbble.auth;

import android.net.Uri;

public class Auth {

    private static final String KEY_CLIENT_ID = "client_id";
    private static final String KEY_SCOPE = "scope";
    private static final String KEY_REDIRECT_URI = "redirect_uri";

    private static final String CLIENT_ID = "b5532a0c2e832bd5650373ab2a54319b0353e5a52eb2108052272f418a092533";
    private static final String CLIENT_SECRET = "f9fd632fb03ac7832057d66bd5c00b99458fab7c0e21dd0516ebbe8051a95a89";

    private static final String SCOPE = "public+write";

    private static final String AUTHORIZE_URI = "https://dribbble.com/oauth/authorize";
    public static final String REDIRECT_URI = "https://www.wadddle.com/";

    public static String getAuthorizeUrl() {
        String url = Uri.parse(AUTHORIZE_URI)
                .buildUpon()
                .appendQueryParameter(KEY_CLIENT_ID, CLIENT_ID)
                .build()
                .toString();

        // fix encode issue
        url += "&" + KEY_REDIRECT_URI + "=" + REDIRECT_URI;
        url += "&" + KEY_SCOPE + "=" + SCOPE;

        return url;
    }
}
