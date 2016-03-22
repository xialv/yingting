package com.yitingche.demo.controller;

import com.michael.corelib.internet.core.json.JsonProperty;

import java.io.Serializable;

/**
 * Created by lvxia on 16/3/17.
 */
public class CardNo implements Serializable {
    @JsonProperty("id")
    public long id; // 主键.
    @JsonProperty("userId")
    public long userId; // 账号.
    @JsonProperty("no")
    public String no; // 车牌号码.
    @JsonProperty("createTime")
    public String createTime; // 创建时间.
    @JsonProperty("updateTime")
    public String updateTime; // 更新时间.
}
