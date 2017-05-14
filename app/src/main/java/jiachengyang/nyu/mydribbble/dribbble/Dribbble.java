package jiachengyang.nyu.mydribbble.dribbble;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import jiachengyang.nyu.mydribbble.model.Bucket;
import jiachengyang.nyu.mydribbble.model.Shot;
import jiachengyang.nyu.mydribbble.model.User;
import jiachengyang.nyu.mydribbble.utils.ModelUtils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Dribbble {

    private static final String API_URL = "https://api.dribbble.com/v1/";

    private static final String USER_END_POINT = API_URL + "user";
    private static final String SHOTS_END_POINT = API_URL + "shots";

    private static String SP_AUTH = "auth"; //preference file name

    private static String KEY_ACCESS_TOKEN = "access_token";
    private static String KEY_USER = "user";

    private static String accessToken;
    private static User user;

    private static final TypeToken<User> USER_TYPE_TOKEN = new TypeToken<User>(){};
    private static final TypeToken<List<Shot>> SHOT_LIST_TYPE_TOKEN = new TypeToken<List<Shot>>(){};
    private static final TypeToken<List<Bucket>> BUCKET_LIST_TYPE_TOKEN = new TypeToken<List<Bucket>>(){};

    public static void init(Context context) {
        accessToken = loadAccessToken(context);
        if (accessToken != null) {
            user = loadUser(context);
        }
    }

    public static void login(Context context, String accessToken) throws IOException {
        Dribbble.accessToken = accessToken;
        storeAccessToken(context, accessToken);

        Dribbble.user = getUser();
        storeUser(context, user);
    }

    public static void logout(Context context) {
        storeAccessToken(context, null);
        storeUser(context, null);

        accessToken = null;
        user = null;
    }

    public static boolean isLoggedIn() {
        return accessToken != null;
    }

    public static User getCurrentUser() {
        return user;
    }

    public static void storeAccessToken(Context context, String accessToken) {
        SharedPreferences sp = context.getApplicationContext()
                .getSharedPreferences(SP_AUTH, Context.MODE_PRIVATE);

        sp.edit().putString(KEY_ACCESS_TOKEN, accessToken).apply();
    }

    public static String loadAccessToken(Context context) {
        SharedPreferences sp = context.getApplicationContext()
                .getSharedPreferences(SP_AUTH, Context.MODE_PRIVATE);

        return sp.getString(KEY_ACCESS_TOKEN, null);
    }

    public static User getUser() throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .addHeader("Authorization", "Bearer " + accessToken)
                .url(USER_END_POINT)
                .build();
        Response response = client.newCall(request).execute();
        String responseString = response.body().string();

        return ModelUtils.toObject(responseString, USER_TYPE_TOKEN);
    }

    public static void storeUser(Context context, User user) {
        ModelUtils.save(context, KEY_USER, user);
    }

    public static User loadUser(Context context) {
        return ModelUtils.read(context, KEY_USER, USER_TYPE_TOKEN);
    }

    public static List<Shot> getShots(int page) throws IOException {
        String url = SHOTS_END_POINT + "?page=" + page;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .addHeader("Authorization", "Bearer " + accessToken)
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        String responseString = response.body().string();

        return ModelUtils.toObject(responseString, SHOT_LIST_TYPE_TOKEN);
    }

    public static List<Bucket> getUserBuckets(int page) throws IOException {
        String url = USER_END_POINT + "/" + "buckets?page=" + page;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .addHeader("Authorization", "Bearer " + accessToken)
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        String responseString = response.body().string();

        //如果某page是空的，response是[]，而不是null
        return ModelUtils.toObject(responseString, BUCKET_LIST_TYPE_TOKEN);
    }
}
