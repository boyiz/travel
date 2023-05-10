package com.xunye.route.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xunye.core.base.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

/**
 * @ClassName RouteImage
 * @Description 路线图片
 * @Author boyiz
 * @Date 2023/4/14 19:15
 * @Version 1.0
 **/

@Data
@Entity
@DynamicUpdate
@Table(name = RouteImage.TABLE_NAME)
public class RouteImage extends BaseEntity {

    public static final String TABLE_NAME = "route_image";

    @Column(name = "route_id", columnDefinition = "varchar(32) comment '关联路线id'")
    private String routeId;
    @Column(name = "day_no", columnDefinition = "int comment '行程的第几天'")
    private Integer dayNo;
    @Column(name = "image_url", columnDefinition = "text comment '图片路径'")
    private String imageUrl;
    @Column(name = "image_type", columnDefinition = "int comment '图片类型'")
    private String imageType;

}
