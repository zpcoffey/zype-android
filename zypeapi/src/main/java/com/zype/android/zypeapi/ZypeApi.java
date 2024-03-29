package com.zype.android.zypeapi;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.zype.android.zypeapi.model.VideoFavoriteResponse;
import com.zype.android.zypeapi.model.VideosResponse;

import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Evgeny Cherkasov on 10.04.2017.
 */

public class ZypeApi {
    private static final String BASE_URL = "https://api.zype.com/";

    // Parameters
    public static final String ACCESS_TOKEN = "access_token";
    public static final String APP_KEY = "app_key";
    private static final String CLIENT_GRANT_TYPE = "grant_type";
    private static final String CLIENT_ID = "client_id";
    public static final String CONSUMER_EMAIL = "consumer[email]";
    public static final String CONSUMER_ID = "consumer_id";
    public static final String CONSUMER_PASSWORD = "consumer[password]";
    private static final String LINKED_DEVICE_ID = "linked_device_id";
    private static final String PAGE = "page";
    private static final String PASSWORD = "password";
    public static final String PER_PAGE = "per_page";
    private static final String PIN = "pin";
    public static final String PLAYLIST_ID_INCLUSIVE = "playlist_id.inclusive";
    public static final String QUERY = "q";
    private static final String REFRESH_TOKEN = "refresh_token";

    public static final String SUBSCRIPTION_CONSUMER_ID = "consumer_id";
    public static final String SUBSCRIPTION_DEVICE_TYPE = "device_type";
    public static final String SUBSCRIPTION_RECEIPT_ID = "receipt_id";
    public static final String SUBSCRIPTION_SHARED_SECRET = "shared_secret";
    public static final String SUBSCRIPTION_THIRD_PARTY_ID = "third_party_id";
    public static final String SUBSCRIPTION_USER_ID = "user_id";

    private static final String USERNAME = "username";
    public static final String UUID = "uuid";

    public static final int PER_PAGE_DEFAULT = 100;

    private static ZypeApi instance;
    private static Retrofit retrofit;
    private static IZypeApi apiImpl;

    private String appKey;

    private ZypeApi() {}

    public static synchronized ZypeApi getInstance() {
        if (instance == null) {
            instance = new ZypeApi();

            // Needs to log retrofit calls
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            apiImpl = retrofit.create(IZypeApi.class);
        }
        return instance;
    }

    public void init(String appKey) {
        this.appKey = appKey;
    }

    public Retrofit retrofit() {
        return retrofit;
    }

    public IZypeApi getApi() {
        if (TextUtils.isEmpty(appKey)) {
            throw new IllegalStateException("You must call 'init()' before accessing API");
        }
        return apiImpl;
    }

//    /*
//     * OAuth
//     */
//    public AccessTokenResponse retrieveAccessToken(String username, String password) {
//        Map<String, String> params = new HashMap<>();
//        params.put(USERNAME, username);
//        params.put(PASSWORD, password);
//        params.put(CLIENT_ID, ZypeSettings.CLIENT_ID);
//        params.put(CLIENT_GRANT_TYPE, "password");
//        try {
//            Response response = apiImpl.retrieveAccessToken(params).execute();
//            if (response.isSuccessful()) {
//                return (AccessTokenResponse) response.body();
//            }
//            else {
//                return null;
//            }
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public AccessTokenResponse retrieveAccessTokenWithPin(String deviceId, String pin) {
//        Map<String, String> params = new HashMap<>();
//        params.put(LINKED_DEVICE_ID, deviceId);
//        params.put(PIN, pin);
//        params.put(CLIENT_ID, ZypeSettings.CLIENT_ID);
//        params.put(CLIENT_GRANT_TYPE, "password");
//        try {
//            Response response = apiImpl.retrieveAccessToken(params).execute();
//            if (response.isSuccessful()) {
//                return (AccessTokenResponse) response.body();
//            }
//            else {
//                return null;
//            }
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public AccessTokenResponse refreshAccessToken(String refreshToken) {
//        Map<String, String> params = new HashMap<>();
//        params.put(REFRESH_TOKEN, refreshToken);
//        params.put(CLIENT_ID, ZypeSettings.CLIENT_ID);
//        params.put(CLIENT_GRANT_TYPE, "refresh_token");
//        try {
//            Response response = apiImpl.retrieveAccessToken(params).execute();
//            if (response.isSuccessful()) {
//                return (AccessTokenResponse) response.body();
//            }
//            else {
//                return null;
//            }
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public AccessTokenInfoResponse getAccessTokenInfo(String accessToken) {
//        try {
//            Response response = apiImpl.getAccessTokenInfo(accessToken).execute();
//            if (response.isSuccessful()) {
//                return (AccessTokenInfoResponse) response.body();
//            }
//            else {
//                return null;
//            }
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public AppResponse getApp() {
//        try {
//            HashMap<String, String> params = new HashMap<>();
//            params.put(APP_KEY, ZypeSettings.APP_KEY);
//            Response response = apiImpl.getApp(params).execute();
//            if (response.isSuccessful()) {
//                return (AppResponse) response.body();
//            }
//            else {
//                return null;
//            }
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public DevicePinResponse getDevicePin(String deviceId) {
//        try {
//            HashMap<String, String> params = new HashMap<>();
//            params.put(APP_KEY, ZypeSettings.APP_KEY);
//            params.put(LINKED_DEVICE_ID, deviceId);
//            Response response = apiImpl.getDevicePin(params).execute();
//            if (response.isSuccessful()) {
//                return (DevicePinResponse) response.body();
//            }
//            else {
//                return null;
//            }
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public DevicePinResponse createDevicePin(String deviceId) {
//        try {
//            HashMap<String, String> params = new HashMap<>();
//            params.put(APP_KEY, ZypeSettings.APP_KEY);
//            params.put(LINKED_DEVICE_ID, deviceId);
//            Response response = apiImpl.createDevicePin(params).execute();
//            if (response.isSuccessful()) {
//                return (DevicePinResponse) response.body();
//            }
//            else {
//                return null;
//            }
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public ConsumerResponse unlinkDevicePin(String consumerId, String pin) {
//        try {
//            HashMap<String, String> params = new HashMap<>();
//            params.put(APP_KEY, ZypeSettings.APP_KEY);
//            params.put(CONSUMER_ID, consumerId);
//            params.put(PIN, pin);
//            Response response = apiImpl.unlinkDevicePin(params).execute();
//            if (response.isSuccessful()) {
//                return (ConsumerResponse) response.body();
//            }
//            else {
//                return null;
//            }
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public ConsumerResponse getConsumer(String consumerId, String accessToken) {
//        try {
//            HashMap<String, String> params = new HashMap<>();
//            params.put(ACCESS_TOKEN, accessToken);
//            Response response = apiImpl.getConsumer(consumerId, params).execute();
//            if (response.isSuccessful()) {
//                return (ConsumerResponse) response.body();
//            }
//            else {
//                return null;
//            }
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public VideoEntitlementsResponse getVideoEntitlements(String accessToken, int page, int perPage) {
//        try {
//            HashMap<String, String> params = new HashMap<>();
//            params.put(ACCESS_TOKEN, accessToken);
//            params.put(PER_PAGE, String.valueOf(perPage));
//            params.put("sort", "created_at");
//            params.put("order", "desc");
//            Response response = apiImpl.getVideoEntitlements(page, params).execute();
//            if (response.isSuccessful()) {
//                return (VideoEntitlementsResponse) response.body();
//            }
//            else {
//                return null;
//            }
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public VideoFavoritesResponse getVideoFavorites(String consumerId, String accessToken, int page) {
//        try {
//            HashMap<String, String> params = new HashMap<>();
//            params.put(ACCESS_TOKEN, accessToken);
//            params.put(PAGE, String.valueOf(page));
//            params.put(PER_PAGE, String.valueOf(PER_PAGE_DEFAULT));
//            params.put("sort", "created_at");
//            params.put("order", "desc");
//            Response response = apiImpl.getVideoFavorites(consumerId, params).execute();
//            if (response.isSuccessful()) {
//                return (VideoFavoritesResponse) response.body();
//            }
//            else {
//                return null;
//            }
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public PlaylistsResponse getPlaylists(int page) {
//        try {
//            HashMap<String, String> params = new HashMap<>();
//            params.put(APP_KEY, ZypeSettings.APP_KEY);
//            params.put(PER_PAGE, String.valueOf(PER_PAGE_DEFAULT));
//            Response response = apiImpl.getPlaylists(page, params).execute();
//            if (response.isSuccessful()) {
//                return (PlaylistsResponse) response.body();
//            }
//            else {
//                return null;
//            }
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public VideosResponse getPlaylistVideos(String playlistId, int page) {
//        try {
//            HashMap<String, String> params = new HashMap<>();
//            params.put(APP_KEY, ZypeSettings.APP_KEY);
//            params.put(PER_PAGE, String.valueOf(PER_PAGE_DEFAULT));
//            Response response = apiImpl.getPlaylistVideos(playlistId, page, params).execute();
//            if (response.isSuccessful()) {
//                return (VideosResponse) response.body();
//            }
//            else {
//                return null;
//            }
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public VideoResponse getVideo(String videoId) {
//        try {
//            HashMap<String, String> params = new HashMap<>();
//            params.put(APP_KEY, ZypeSettings.APP_KEY);
//            Response response = apiImpl.getVideo(videoId, params).execute();
//            if (response.isSuccessful()) {
//                return (VideoResponse) response.body();
//            }
//            else {
//                return null;
//            }
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public VideosResponse searchVideos(String query) {
//        try {
//            HashMap<String, String> params = new HashMap<>();
//            params.put(APP_KEY, ZypeSettings.APP_KEY);
//            params.put(PER_PAGE, String.valueOf(PER_PAGE_DEFAULT));
//            params.put(QUERY, query);
//            Response response = apiImpl.getVideos(1, params).execute();
//            if (response.isSuccessful()) {
//                return (VideosResponse) response.body();
//            }
//            else {
//                return null;
//            }
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    public void addVideoFavorite(@NonNull String accessToken, String consumerId, @NonNull String videoId,
                                 @NonNull final IZypeApiListener listener) {
        HashMap<String, String> queryParams = new HashMap<>();
        queryParams.put(ZypeApi.ACCESS_TOKEN, accessToken);
        HashMap<String, String> fieldParams = new HashMap<>();
        fieldParams.put("video_id", videoId);
        ZypeApi.getInstance().getApi().addVideoFavorite(consumerId, queryParams, fieldParams)
                .enqueue(new Callback<VideoFavoriteResponse>() {
                    @Override
                    public void onResponse(Call<VideoFavoriteResponse> call, Response<VideoFavoriteResponse> response) {
                        if (response.isSuccessful()) {
                            listener.onCompleted(new ZypeApiResponse<VideoFavoriteResponse>(response.body(), true));
                        }
                        else {
                            listener.onCompleted(new ZypeApiResponse<VideoFavoriteResponse>(response.body(), false));
                        }
                    }

                    @Override
                    public void onFailure(Call<VideoFavoriteResponse> call, Throwable t) {
                        listener.onCompleted(new ZypeApiResponse<VideoFavoriteResponse>(null, false));
                    }
                });
    }


    public void getPlaylistVideos(@NonNull String playlistId, int page, @NonNull final IZypeApiListener listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put(ZypeApi.APP_KEY, appKey);
        params.put(ZypeApi.PER_PAGE, String.valueOf(ZypeApi.PER_PAGE_DEFAULT));
        getApi().getPlaylistVideos(playlistId, page, params).enqueue(new Callback<VideosResponse>() {
            @Override
            public void onResponse(Call<VideosResponse> call, Response<VideosResponse> response) {
                listener.onCompleted(new ZypeApiResponse<VideosResponse>(response.body(), true));
            }

            @Override
            public void onFailure(Call<VideosResponse> call, Throwable t) {
                listener.onCompleted(new ZypeApiResponse<VideosResponse>(null, false));
            }
        });
    }

    public void searchVideos(@NonNull String query, String playlistId,
                             int page, @NonNull final IZypeApiListener listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put(ZypeApi.QUERY, query);
        if (!TextUtils.isEmpty(playlistId)) {
            params.put(PLAYLIST_ID_INCLUSIVE, playlistId);
        }
        params.put(ZypeApi.APP_KEY, appKey);
        params.put(ZypeApi.PAGE, String.valueOf(page));
        params.put(ZypeApi.PER_PAGE, String.valueOf(ZypeApi.PER_PAGE_DEFAULT));
        getApi().getVideos(params).enqueue(new Callback<VideosResponse>() {
            @Override
            public void onResponse(Call<VideosResponse> call, Response<VideosResponse> response) {
                listener.onCompleted(new ZypeApiResponse<VideosResponse>(response.body(), true));
            }

            @Override
            public void onFailure(Call<VideosResponse> call, Throwable t) {
                listener.onCompleted(new ZypeApiResponse<VideosResponse>(null, false));
            }
        });
    }

}
