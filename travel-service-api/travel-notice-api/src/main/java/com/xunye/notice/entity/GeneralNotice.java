package com.xunye.notice.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xunye.core.base.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

/**
 * @ClassName GeneralNotice
 * @Description 通用注意事项
 * @Author boyiz
 * @Date 2023/4/13 18:03
 * @Version 1.0
 **/

@Data
@Entity
@DynamicUpdate
@Table(name = GeneralNotice.TABLE_NAME)
public class GeneralNotice extends BaseEntity {

    public static final String TABLE_NAME = "general_notice";

    @Column(name = "expense_exclude", columnDefinition = "text comment '费用不含'")
    private String expenseExclude;

    @Column(name = "expense_include", columnDefinition = "text comment '费用包含'")
    private String expenseInclude;

    @Column(name = "refund_notice", columnDefinition = "text comment '退费规则'")
    private String refundNotice;

    @Column(name = "description", columnDefinition = "text comment '其他描述'")
    private String description;


}
