package com.xunye.auth.unserialize;

import java.io.Serializable;
import java.util.List;

import com.xunye.auth.dto.UserDTO;
import lombok.Data;

/**
 * @ClassName UserUnserializeJsonRootEntity
 * @Description 为Redis中存储的用户JSON信息进行反序列化
 * @Author boyiz
 * @Date 2023/4/20 17:52
 * @Version 1.0
 **/
@Data
public class UserUnserializeJsonRootEntity implements Serializable {
    private String id;
    private long createTime;
    private DataMap dataMap;
    private List<String> tokenSignList;
}
