package com.xunye.core.base;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * @ClassName BaseAutoIncreEntity
 * @Description TODO
 * @Author boyiz
 * @Date 2023/4/13 17:29
 * @Version 1.0
 **/

@Data
@MappedSuperclass
public abstract class BaseAutoIncreEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /* 过滤logic删除，存在的数据 */
    public static final String LOGIC_DELETE_CLAUSE_EXIST = "1=1 and (is_delete = 0 or is_delete is null)";

    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "varchar(32) comment 'id'")
    private String id;

    /* 创建人ID */
    @Column(name = "create_by", columnDefinition = " varchar(64) comment '创建人ID'")
    private String createBy;

    /* 创建人名称 */
    @Column(name = "create_by_name", columnDefinition = " varchar(128) comment '创建人员'")
    private String createByName;

    /* 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    @CreationTimestamp
    @Column(name = "create_time", columnDefinition = " datetime(3) default null comment '创建时间'")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /* 修改人ID */
    @Column(name = "update_by", columnDefinition = " varchar(64) comment '修改人ID'")
    private String updateBy;

    /* 修改人员名称 */
    @Column(name = "update_by_name", columnDefinition = " varchar(128) comment '修改人员'")
    private String updateByName;

    /* 修改时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @UpdateTimestamp
    @Column(name = "update_time", columnDefinition = " datetime(3) default null comment '修改时间'")
    private Date updateTime;
}
