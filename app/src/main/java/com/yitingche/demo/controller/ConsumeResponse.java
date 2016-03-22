package com.yitingche.demo.controller;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lvxia on 16/3/11.
 */
public class ConsumeResponse implements Serializable {
    public List<ConsumeItem> ConsumeList;
    public static class ConsumeItem {
        public long id; // 主键.
        public String carNo; // 车牌号码.
        public float money; // 消费金额.
        public String way; // 扣费方式.
        public java.util.Date inTime; // 驶入时间.
        public java.util.Date outTime; // 驶出时间.
        public long parkId; // 停车场ID.
        public java.util.Date createTime; // 创建时间.
        public java.util.Date updateTime; // 更新时间.
    }
}
