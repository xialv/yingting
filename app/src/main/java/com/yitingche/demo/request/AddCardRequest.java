package com.yitingche.demo.request;

import com.michael.corelib.internet.core.annotations.HttpHeaderParam;
import com.michael.corelib.internet.core.annotations.HttpMethod;
import com.michael.corelib.internet.core.annotations.RequiredParam;
import com.michael.corelib.internet.core.annotations.RestMethodUrl;

/**
 * Created by lvxia on 16/3/8.
 */

@RestMethodUrl("addCarNo.rs")
@HttpMethod("POST")
public class AddCardRequest extends RequestBase<String>{
    @HttpHeaderParam("Content-Type")
    private String content_type = "application/json";

    @RequiredParam("userId")
    private long userId;

    @RequiredParam("no")
    private String no;

    public AddCardRequest(long id, String no){
        this.userId = id;
        this.no = no;
    }
}
