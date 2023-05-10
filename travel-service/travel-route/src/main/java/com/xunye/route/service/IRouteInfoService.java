package com.xunye.route.service;

import com.xunye.auth.entity.User;
import com.xunye.core.result.R;
import com.xunye.route.dto.RouteInfoDTO;
import com.xunye.route.dto.RouteInfoEditDTO;
import com.xunye.route.entity.RouteInfo;
import com.xunye.core.base.BaseService;
import com.xunye.core.model.PredicateWrapper;
import com.xunye.route.vo.RouteDetailVo;
import org.springframework.data.domain.Pageable;

import com.querydsl.core.types.Predicate;

import java.util.List;

/**
 * 路线Service接口
 *
 * @Author: boyiz
 * @Date: 2023-04-12
 */
public interface IRouteInfoService extends BaseService<RouteInfoEditDTO, RouteInfoDTO, RouteInfo,String> {

    /**
     * 创建路线
     *
     * @param createDto 编辑DTO
     * @param operatorUser 当前操作用户
     * @return ID
     */
    String createRouteInfo(RouteInfoEditDTO createDto, User operatorUser);

    /**
     * 更新路线
     *
     * @param updateDto 编辑DTO
     * @param operatorUser 当前操作用户
     */
    void updateRouteInfo(RouteInfoEditDTO updateDto, User operatorUser);

    /**
     * 根据ID删除路线
     *
     * @param id 路线ID
     */
    void deleteRouteInfo(String id, User operatorUser);

    /**
     * 批量删除路线
     *
     * @param ids 路线ID集合
     */
    void deleteRouteInfoBatch(List<String> ids, User operatorUser);

    /**
     * 列表分页查询路线
     *
     * @param predicateWrapper 条件谓语包装类
     * @param pageable         分页参数
     * @return 统一结果集
     */
    List<RouteDetailVo> queryRouteInfoListByPage(PredicateWrapper predicateWrapper, Pageable pageable);

    /**
     * 根据ID查询路线
     *
     * @param id ID
     * @return 编辑DTO
     */
    RouteInfoEditDTO queryRouteInfoById(String id);


    /**
     * 导出路线Excel
     *
     * @param predicateWrapper 查询条件包装类
     * @return 数据集合
     */
    List<RouteDetailVo> export(PredicateWrapper predicateWrapper);

    // ======================================== 以上由代码生成器生成 ======================================== //

    /**
     * 根据predicate查询
     *
     * @param predicate 查询条件
     * @return 数据集合
     */
    List<RouteInfoEditDTO> queryRouteInfoEditDTOListByPredicate(Predicate predicate);

    RouteDetailVo queryRouteInfoByRouteId(String routeId);
}
