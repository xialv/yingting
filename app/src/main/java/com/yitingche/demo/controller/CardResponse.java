package com.yitingche.demo.controller;

import com.michael.corelib.internet.core.ResponseBase;
import com.michael.corelib.internet.core.json.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lvxia on 16/3/17.
 */
public class CardResponse extends ResponseBase {
    @JsonProperty("carNo")
    public List<CardNo> carNo;
}
