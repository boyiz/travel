package com.xunye.notice.dto;

import lombok.Data;
import java.util.Date;

/**
 * 路线注意事项新增/编辑DTO
 *
 * @Author: boyiz
 * @Date: 2023-04-14
 */
@Data
public class RouteNoticeEditDTO {

    /* id */
    private String id;
    /* 关联路线id */
    private String routeId;
    /* 准备事宜 */
    private String preparations;
    /* 关于当地 */
    private String aboutLocal;
    /* 关于交通 */
    private String aboutTraffic;
    /* 其他事宜 */
    private String otherIssues;
    /* 备注 */
    private String remark;
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
