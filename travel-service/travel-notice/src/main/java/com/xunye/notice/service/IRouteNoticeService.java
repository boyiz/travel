package com.xunye.notice.service;

import com.xunye.notice.dto.RouteNoticeDTO;
import com.xunye.notice.dto.RouteNoticeEditDTO;
import com.xunye.notice.entity.RouteNotice;
import com.xunye.core.base.BaseService;
import com.xunye.core.model.PredicateWrapper;
import com.xunye.core.result.R;
import org.springframework.data.domain.Pageable;

import com.xunye.auth.entity.User;
import com.querydsl.core.types.Predicate;

import java.util.List;

/**
 * 路线注意事项Service接口
 *
 * @Author: boyiz
 * @Date: 2023-04-14
 */
public interface IRouteNoticeService extends BaseService<RouteNoticeEditDTO, RouteNoticeDTO, RouteNotice,String> {

    /**
     * 创建路线注意事项
     *
     * @param createDto 编辑DTO
     * @param operatorUser 当前操作用户
     * @return ID
     */
    String createRouteNotice(RouteNoticeEditDTO createDto, User operatorUser);

    /**
     * 更新路线注意事项
     *
     * @param updateDto 编辑DTO
     * @param operatorUser 当前操作用户
     */
    void updateRouteNotice(RouteNoticeEditDTO updateDto, User operatorUser);

    /**
     * 根据ID删除路线注意事项
     *
     * @param id 路线注意事项ID
     */
    void deleteRouteNotice(String id, User operatorUser);

    /**
     * 批量删除路线注意事项
     *
     * @param ids 路线注意事项ID集合
     */
    void deleteRouteNoticeBatch(List<String> ids, User operatorUser);

    /**
     * 列表分页查询路线注意事项
     *
     * @param predicateWrapper 条件谓语包装类
     * @param pageable         分页参数
     * @return 统一结果集
     */
    R<List<RouteNoticeDTO>> queryRouteNoticeListByPage(PredicateWrapper predicateWrapper, Pageable pageable);

    /**
     * 根据ID查询路线注意事项
     *
     * @param id ID
     * @return 编辑DTO
     */
    RouteNoticeEditDTO queryRouteNoticeById(String id);


    /**
     * 导出路线注意事项Excel
     *
     * @param predicateWrapper 查询条件包装类
     * @return 数据集合
     */
    List<RouteNoticeDTO> export(PredicateWrapper predicateWrapper);

    // ======================================== 以上由代码生成器生成 ======================================== //

    /**
     * 根据predicate查询
     *
     * @param predicate 查询条件
     * @return 数据集合
     */
    List<RouteNoticeEditDTO> queryRouteNoticeEditDTOListByPredicate(Predicate predicate);

    RouteNoticeEditDTO queryRouteNoticeByRouteId(String routeId);

}
