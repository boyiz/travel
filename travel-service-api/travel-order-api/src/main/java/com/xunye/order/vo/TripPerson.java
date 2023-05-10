package com.xunye.order.vo;

import lombok.Data;

/**
 * @ClassName TripPerson
 * @Description 创建订单时的出行人信息
 * @Author boyiz
 * @Date 2023/5/3 17:12
 * @Version 1.0
 **/
@Data
public class TripPerson {

    private String userId;
    private Integer userType;

}
