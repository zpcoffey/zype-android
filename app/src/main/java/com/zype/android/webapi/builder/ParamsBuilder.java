package com.zype.android.webapi.builder;

import android.os.Bundle;

import java.util.HashMap;

public class ParamsBuilder {

    public static final String APP_KEY = "app_key";

    public static final String POST_PARAMS = "post";

    public static final String PATH_PARAMS = "url";

    public static final String GET_PARAMS = "get";

//    public static final String RECEIVER_PARAMS = "receiver";

    private HashMap<String, String> mPostParams;
    private HashMap<String, String> mPathParams;
    private HashMap<String, String> mGetParams;

//    private Parcelable mReceiver;

    public ParamsBuilder() {
        mPostParams = new HashMap<>();
        mPathParams = new HashMap<>();
        mGetParams = new HashMap<>();
    }

    public ParamsBuilder addPostParam(String key, String value) {
        mPostParams.put(key, value);
        return this;
    }

    public ParamsBuilder addPathParam(String key, String value) {
        mPathParams.put(key, value);
        return this;
    }

    public ParamsBuilder addGetParam(String key, String value) {
        mGetParams.put(key, value);
        return this;
    }

//    public ParamsBuilder addReceiver(Parcelable receiver) {
//        mReceiver = receiver;
//        return this;
//    }

    public Bundle build() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(POST_PARAMS, mPostParams);
        bundle.putSerializable(PATH_PARAMS, mPathParams);
        bundle.putSerializable(GET_PARAMS, mGetParams);
//        bundle.putParcelable(RECEIVER_PARAMS, mReceiver);
        return bundle;
    }
}