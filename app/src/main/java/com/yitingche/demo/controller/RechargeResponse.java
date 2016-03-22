package com.yitingche.demo.controller;

import java.io.Serializable;

/**
 * Created by lvxia on 16/3/11.
 */
public class RechargeResponse extends Object implements Serializable {
    private long id; // 主键.
    private long userId; // 充值账号.
    private float money; // 充值金额.
    private String way; // 充值途径. 如：工商银行、支付宝
    private String fromCardNo; // 充值来源卡号.
    private java.util.Date createTime; // 创建时间.
    private java.util.Date updateTime; // 更新时间.
}

