package com.yitingche.demo.request;

import com.michael.corelib.internet.core.annotations.HttpHeaderParam;
import com.michael.corelib.internet.core.annotations.HttpMethod;
import com.michael.corelib.internet.core.annotations.RequiredParam;
import com.michael.corelib.internet.core.annotations.RestMethodUrl;

/**
 * Created by lvxia on 16/3/8.
 */

@RestMethodUrl("queryDepositRecordByUserId.rs")
@HttpMethod("POST")
public class RechargeRequest extends RequestBase<String>{
    @HttpHeaderParam("Content-Type")
    private String content_type = "application/json";

    @RequiredParam("id")
    private long id;

    public RechargeRequest(long id){
        this.id = id;
    }
}
