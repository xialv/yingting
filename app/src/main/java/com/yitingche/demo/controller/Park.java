package com.yitingche.demo.controller;

import java.io.Serializable;

/**
 * Created by lvxia on 16/3/17.
 */
public class Park implements Serializable {
    public long id; // 主键.
    public String code; // 停车场编码.
    public String name; // 停车场名称.
    public String addr; // 地址.
    public int seatNum; // 车位数量.
    public int freeSeatNum; // 空闲车位数量.
    public Double coordinateX; // 坐标X.
    public Double coordinateY; // 坐标Y.
    public String createTime; // 创建时间.
    public String updateTime; // 更新时间.
}

