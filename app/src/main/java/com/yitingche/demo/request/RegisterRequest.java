package com.yitingche.demo.request;

import com.michael.corelib.internet.core.annotations.HttpHeaderParam;
import com.michael.corelib.internet.core.annotations.HttpMethod;
import com.michael.corelib.internet.core.annotations.RequiredParam;
import com.michael.corelib.internet.core.annotations.RestMethodUrl;

/**
 * Created by lvxia on 16/3/8.
 */

@RestMethodUrl("registUser.rs")
@HttpMethod("POST")
public class RegisterRequest extends RequestBase<String>{
    @HttpHeaderParam("Content-Type")
    private String content_type = "application/json";

    @RequiredParam("account")
    private String account;
    @RequiredParam("password")
    private String password;
    @RequiredParam("name")
    private String name;
    @RequiredParam("status")
    private int status=0;
    @RequiredParam("grade")
    private int grade=0;

    public RegisterRequest(String account, String password, String name){
        this.account = account;
        this.password = password;
        this.name = name;
    }
}
