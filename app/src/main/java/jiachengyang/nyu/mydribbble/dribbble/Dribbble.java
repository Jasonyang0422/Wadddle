package jiachengyang.nyu.mydribbble.dribbble;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.IOException;

import jiachengyang.nyu.mydribbble.model.User;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Dribbble {

    private static final String API_URL = "https://api.dribbble.com/v1/";

    private static final String USER_END_POINT = API_URL + "user";

    private static String SP_AUTH = "auth"; //preference file name

    private static String KEY_ACCESS_TOKEN = "access_token";

    private static String accessToken;
    private static User user;

    public static void login(Context context, String accessToken) throws IOException {
        Dribbble.accessToken = accessToken;
        storeAccessToken(context, accessToken);

        String user_json_string = getUser();
        Log.i("Jason-accesstoken", accessToken);
        Log.i("Jason-userjsonstring", user_json_string);

//        Dribbble.user = getUser();
//        storeUser(context, user);
    }

    public static void storeAccessToken(Context context, String accessToken) {
        SharedPreferences sp = context.getApplicationContext()
                .getSharedPreferences(SP_AUTH, Context.MODE_PRIVATE);

        sp.edit().putString(KEY_ACCESS_TOKEN, accessToken).apply();
    }

    public static String getUser() throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .addHeader("Authorization", "Bearer " + accessToken)
                .url(USER_END_POINT)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
