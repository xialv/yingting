package com.yitingche.demo.request;

import com.michael.corelib.internet.core.annotations.HttpHeaderParam;
import com.michael.corelib.internet.core.annotations.HttpMethod;
import com.michael.corelib.internet.core.annotations.RequiredParam;
import com.michael.corelib.internet.core.annotations.RestMethodUrl;

/**
 * Created by lvxia on 16/3/17.
 */
@RestMethodUrl("queryParkBycCoordinate.rs")
@HttpMethod("POST")
public class ParkRequest extends RequestBase<String> {
    @HttpHeaderParam("Content-Type")
    private String content_type = "application/json";

    @RequiredParam("x")
    private String x;
    @RequiredParam("y")
    private String y;
    @RequiredParam("returnParkMaxNum")
    private int returnParkMaxNum;

    public ParkRequest(String x, String y, int max){
        this.x = x;
        this.y = y;
        this.returnParkMaxNum = max;
    }
}

