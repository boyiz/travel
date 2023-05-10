package com.xunye.order.event;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

/**
 * @ClassName GroupStockUpdateEvent
 * @Description
 * @Author boyiz
 * @Date 2023/5/9 11:31
 * @Version 1.0
 **/
@Getter
@Setter
@ToString
public class GroupStockUpdateEvent extends ApplicationEvent implements Serializable  {

    private String groupId;
    private int number;
    public GroupStockUpdateEvent(Object source , String groupId , int number) {
        super(source);
        this.groupId = groupId;
        this.number = number;
    }
}
