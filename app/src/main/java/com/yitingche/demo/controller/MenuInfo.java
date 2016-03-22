package com.yitingche.demo.controller;

/**
 * Created by lvxia on 16/3/7.
 */
public class MenuInfo {
    public String menuName;
    public String menuDesc;
    public int operatorType;

    public static final int TYPE_RECHARGE = 0;
    public static final int TYPE_DEAL = 1;
    public static final int TYPE_CARD = 2;

    public MenuInfo(String name, String desc, int type){
        this.menuName = name;
        this.menuDesc = desc;
        this.operatorType = type;
    }
}
