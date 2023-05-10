package com.xunye.core.base;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

// 触发更新
@Data
public abstract class BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /* 主键ID */
    private String id;

    /* 创建人 */
    private String createBy;

    /* 创建人名称 */
    private String createByName;

    /* 创建时间 */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /* 更新人 */
    private String updateBy;

    /* 创建/更新者 */
    private String updateByName;

    /* 创建/更新时间 */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

}
