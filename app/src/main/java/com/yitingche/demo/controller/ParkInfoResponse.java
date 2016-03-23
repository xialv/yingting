package com.yitingche.demo.controller;

import java.util.List;

/**
 * Created by lvxia on 16/3/17.
 */
public class ParkInfoResponse {
    public List<ParkService> parkServices;
    public ReteRuleInfo reteRuleInfo;

    public static class ParkService{
        public String content;
        public String createTime;
        public long id;
        public long parkId;
        public String title;
        public String updateTime;
    }

//    "cap": "Y",
//            "capPrice": 50,
//            "capSection": 24,
//            "containFreeTime": "Y",
//            "freeTime": 30,
//            "timeSegLst": [
//    {
//        "endTime": "08:00:00",
//            "id": 1,
//            "orderNum": 1,
//            "rateRule": null,
//            "rateRuleId": 1,
//            "startTime": "00:00:00",
//            "unitPrice": 2,
//            "unitTime": 60
//    },
    public static class ReteRuleInfo{
    public String cap;
    public int capPrice;
    public String capSection;
    public String containFreeTime;
    public String freeTime;
    public List<TimeSeg> timeSegLst;
    }

    public static class TimeSeg{
        public String endTime;
        public long id;
        public String rateRule;
        public String rateRuleId;
        public String startTime;
        public String unitPrice;
        public String unitTime;
    }
}
