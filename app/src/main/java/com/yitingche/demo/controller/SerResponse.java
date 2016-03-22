package com.yitingche.demo.controller;

import java.io.Serializable;

/**
 * Created by lvxia on 16/3/10.
 */
public class SerResponse implements Serializable {
    public boolean rs;   // 接口方法执行是否成功.true:成功;false:失败.
    public String msg;   // 信息(提示信息或错误信息)

    // 以下属性在调用不同方法时会分别赋值(调用接口方法处有说明).
    public long id;      // 数据ID
    private double value; // 值域
    private boolean bln;  // 逻辑值
}
