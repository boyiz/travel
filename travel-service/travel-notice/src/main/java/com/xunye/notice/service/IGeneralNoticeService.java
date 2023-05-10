package com.xunye.notice.service;

import com.xunye.notice.dto.GeneralNoticeDTO;
import com.xunye.notice.dto.GeneralNoticeEditDTO;
import com.xunye.notice.entity.GeneralNotice;
import com.xunye.core.base.BaseService;
import com.xunye.core.model.PredicateWrapper;
import com.xunye.core.result.R;
import org.springframework.data.domain.Pageable;

import com.xunye.auth.entity.User;
import com.querydsl.core.types.Predicate;

import java.util.List;

/**
 * 通用注意事项Service接口
 *
 * @Author: boyiz
 * @Date: 2023-04-13
 */
public interface IGeneralNoticeService extends BaseService<GeneralNoticeEditDTO, GeneralNoticeDTO, GeneralNotice,String> {

    /**
     * 创建通用注意事项
     *
     * @param createDto 编辑DTO
     * @param operatorUser 当前操作用户
     * @return ID
     */
    String createGeneralNotice(GeneralNoticeEditDTO createDto, User operatorUser);

    /**
     * 更新通用注意事项
     *
     * @param updateDto 编辑DTO
     * @param operatorUser 当前操作用户
     */
    void updateGeneralNotice(GeneralNoticeEditDTO updateDto, User operatorUser);

    /**
     * 根据ID删除通用注意事项
     *
     * @param id 通用注意事项ID
     */
    void deleteGeneralNotice(String id, User operatorUser);

    /**
     * 批量删除通用注意事项
     *
     * @param ids 通用注意事项ID集合
     */
    void deleteGeneralNoticeBatch(List<String> ids, User operatorUser);

    /**
     * 列表分页查询通用注意事项
     *
     * @param predicateWrapper 条件谓语包装类
     * @param pageable         分页参数
     * @return 统一结果集
     */
    R<List<GeneralNoticeDTO>> queryGeneralNoticeListByPage(PredicateWrapper predicateWrapper, Pageable pageable);

    /**
     * 根据ID查询通用注意事项
     *
     * @param id ID
     * @return 编辑DTO
     */
    GeneralNoticeEditDTO queryGeneralNoticeById(String id);


    /**
     * 导出通用注意事项Excel
     *
     * @param predicateWrapper 查询条件包装类
     * @return 数据集合
     */
    List<GeneralNoticeDTO> export(PredicateWrapper predicateWrapper);

    // ======================================== 以上由代码生成器生成 ======================================== //

    /**
     * 根据predicate查询
     *
     * @param predicate 查询条件
     * @return 数据集合
     */
    List<GeneralNoticeEditDTO> queryGeneralNoticeEditDTOListByPredicate(Predicate predicate);

}
