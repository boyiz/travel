package com.xunye.notice.dto;

import lombok.Data;
import java.util.Date;

/**
 * 通用注意事项新增/编辑DTO
 *
 * @Author: boyiz
 * @Date: 2023-04-13
 */
@Data
public class GeneralNoticeEditDTO {

    /* id */
    private String id;
    /* 费用不含 */
    private String expenseExclude;
    /* 费用包含 */
    private String expenseInclude;
    /* 退费规则 */
    private String refundNotice;
    /* 其他描述 */
    private String description;
    /* 创建人ID */
    private String createBy;
    /* 创建人员 */
    private String createByName;
    /* 创建时间 */
    private Date createTime;
    /* 修改人ID */
    private String updateBy;
    /* 修改人员 */
    private String updateByName;
    /* 修改时间 */
    private Date updateTime;

}


