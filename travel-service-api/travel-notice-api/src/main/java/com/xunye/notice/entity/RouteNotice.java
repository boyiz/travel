package com.xunye.notice.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xunye.core.base.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

/**
 * @ClassName RouteNotice
 * @Description 路线注意事项
 * @Author boyiz
 * @Date 2023/4/14 12:35
 * @Version 1.0
 **/

@Data
@Entity
@DynamicUpdate
@Table(name = RouteNotice.TABLE_NAME)
public class RouteNotice extends BaseEntity {

    public static final String TABLE_NAME = "route_notice";

    @Column(name = "route_id", columnDefinition = "varchar(32) comment '关联路线id'")
    private String routeId;
    @Column(name = "preparations", columnDefinition = "text comment '准备事宜'")
    private String preparations;
    @Column(name = "about_local", columnDefinition = "text comment '关于当地'")
    private String aboutLocal;
    @Column(name = "about_traffic", columnDefinition = "text comment '关于交通'")
    private String aboutTraffic;
    @Column(name = "other_issues", columnDefinition = "text comment '其他事宜'")
    private String otherIssues;
    @Column(name = "remark", columnDefinition = "text comment '备注'")
    private String remark;

}
