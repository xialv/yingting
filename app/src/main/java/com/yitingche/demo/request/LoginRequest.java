package com.yitingche.demo.request;

import com.michael.corelib.internet.core.annotations.HttpHeaderParam;
import com.michael.corelib.internet.core.annotations.HttpMethod;
import com.michael.corelib.internet.core.annotations.RequiredParam;
import com.michael.corelib.internet.core.annotations.RestMethodUrl;

/**
 * Created by lvxia on 16/3/8.
 */

@RestMethodUrl("login.rs")
@HttpMethod("POST")
public class LoginRequest extends RequestBase<String>{
    @HttpHeaderParam("Content-Type")
    private String content_type = "application/json";

    @RequiredParam("account")
    private String account;
    @RequiredParam("password")
    private String password;

    public LoginRequest(String account, String password){
        this.account = account;
        this.password = password;
    }
}
