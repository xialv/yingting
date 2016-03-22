package com.yitingche.demo.event;

/**
 * Created by lvxia on 16/3/10.
 */
public class ResultEvent {
    public int state = 0;
    public Object result;
    public int from;

    public static final int FROM_RECHARGE = 0;
    public static final int FROM_CONSUME = 1;

    public ResultEvent(){}
    public ResultEvent(int state, Object result, int from){
        this.state = state;
        this.result = result;
        this.from = from;
    }
}
