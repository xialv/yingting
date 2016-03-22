package com.yitingche.demo.request;

import android.os.Bundle;
import android.text.TextUtils;
import com.michael.corelib.internet.core.NetWorkException;

public class RequestBase<T> extends com.michael.corelib.internet.core.RequestBase<T> {

    //base api
    private static String API_RELEASE_URL = "http://101.200.240.228/centerServer/app/";

    private static final String KEY_METHOD = "method";
    private static final String KEY_HTTP_METHOD = "httpMethod";


    @Override
    protected Bundle getParams() throws NetWorkException {
        Bundle params = super.getParams();

        String method = params.getString(KEY_METHOD);
        if (TextUtils.isEmpty(method)) {
            throw new RuntimeException("Method Name MUST NOT be NULL");
        }

        if (!method.startsWith("http://") && !method.startsWith("https://")) {
            method = getBaseApiUrl() + method;
        }

        String httpMethod = params.getString(KEY_HTTP_METHOD);
        params.remove(KEY_HTTP_METHOD);
        params.remove(KEY_METHOD);

        params.putString(KEY_METHOD, method);
        params.putString(KEY_HTTP_METHOD, httpMethod);

        return params;
    }


    public static String getBaseApiUrl() {
        return API_RELEASE_URL;
    }
}
