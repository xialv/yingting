package com.yitingche.demo.event;

/**
 * Created by lvxia on 16/3/10.
 */
public class LoginEvent {
    public boolean loginSuccess = false;
    public String message = "";
    public long userId = 0L;
    public String account = "";

    public LoginEvent(){}
    public LoginEvent(boolean loginSuccess, String message){
        this.loginSuccess = loginSuccess;
        this.message = message;
    }
}
