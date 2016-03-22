package com.yitingche.demo.event;

import com.yitingche.demo.controller.Park;

import java.util.List;

/**
 * Created by lvxia on 16/3/17.
 */
public class ParkEvent {
    public int state = 0;
    public List<Park> result;

    public ParkEvent(){}
    public ParkEvent(int state, List<Park> result){
        this.state = state;
        this.result = result;
    }
}
