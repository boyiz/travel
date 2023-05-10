package com.xunye.route.service;

import com.xunye.route.dto.RouteScheduleDTO;
import com.xunye.route.dto.RouteScheduleEditDTO;
import com.xunye.route.entity.RouteSchedule;
import com.xunye.core.base.BaseService;
import com.xunye.core.model.PredicateWrapper;
import com.xunye.core.result.R;
import org.springframework.data.domain.Pageable;

import com.xunye.auth.entity.User;
import com.querydsl.core.types.Predicate;

import java.util.List;

/**
 * 路线-每天计划Service接口
 *
 * @Author: boyiz
 * @Date: 2023-04-15
 */
public interface IRouteScheduleService extends BaseService<RouteScheduleEditDTO, RouteScheduleDTO, RouteSchedule,String> {

    /**
     * 创建路线-每天计划
     *
     * @param createDto 编辑DTO
     * @param operatorUser 当前操作用户
     * @return ID
     */
    String createRouteSchedule(RouteScheduleEditDTO createDto, User operatorUser);

    /**
     * 更新路线-每天计划
     *
     * @param updateDto 编辑DTO
     * @param operatorUser 当前操作用户
     */
    void updateRouteSchedule(RouteScheduleEditDTO updateDto, User operatorUser);

    /**
     * 根据ID删除路线-每天计划
     *
     * @param id 路线-每天计划ID
     */
    void deleteRouteSchedule(String id, User operatorUser);

    /**
     * 批量删除路线-每天计划
     *
     * @param ids 路线-每天计划ID集合
     */
    void deleteRouteScheduleBatch(List<String> ids, User operatorUser);

    /**
     * 列表分页查询路线-每天计划
     *
     * @param predicateWrapper 条件谓语包装类
     * @param pageable         分页参数
     * @return 统一结果集
     */
    R<List<RouteScheduleDTO>> queryRouteScheduleListByPage(PredicateWrapper predicateWrapper, Pageable pageable);

    /**
     * 根据ID查询路线-每天计划
     *
     * @param id ID
     * @return 编辑DTO
     */
    RouteScheduleEditDTO queryRouteScheduleById(String id);

    List<RouteScheduleEditDTO>  queryRouteScheduleByRouteId(String routeId);


    /**
     * 导出路线-每天计划Excel
     *
     * @param predicateWrapper 查询条件包装类
     * @return 数据集合
     */
    List<RouteScheduleDTO> export(PredicateWrapper predicateWrapper);

    // ======================================== 以上由代码生成器生成 ======================================== //

    /**
     * 根据predicate查询
     *
     * @param predicate 查询条件
     * @return 数据集合
     */
    List<RouteScheduleEditDTO> queryRouteScheduleEditDTOListByPredicate(Predicate predicate);

}
