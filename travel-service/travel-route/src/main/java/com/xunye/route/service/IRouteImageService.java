package com.xunye.route.service;

import com.xunye.route.dto.RouteImageDTO;
import com.xunye.route.dto.RouteImageEditDTO;
import com.xunye.route.entity.RouteImage;
import com.xunye.core.base.BaseService;
import com.xunye.core.model.PredicateWrapper;
import com.xunye.core.result.R;
import org.springframework.data.domain.Pageable;

import com.xunye.auth.entity.User;
import com.querydsl.core.types.Predicate;

import java.util.List;

/**
 * 路线介绍图Service接口
 *
 * @Author: boyiz
 * @Date: 2023-04-18
 */
public interface IRouteImageService extends BaseService<RouteImageEditDTO, RouteImageDTO, RouteImage,String> {

    /**
     * 创建路线介绍图
     *
     * @param createDto 编辑DTO
     * @param operatorUser 当前操作用户
     * @return ID
     */
    String createRouteImage(RouteImageEditDTO createDto, User operatorUser);

    /**
     * 更新路线介绍图
     *
     * @param updateDto 编辑DTO
     * @param operatorUser 当前操作用户
     */
    void updateRouteImage(RouteImageEditDTO updateDto, User operatorUser);

    /**
     * 根据ID删除路线介绍图
     *
     * @param id 路线介绍图ID
     */
    void deleteRouteImage(String id, User operatorUser);

    /**
     * 批量删除路线介绍图
     *
     * @param ids 路线介绍图ID集合
     */
    void deleteRouteImageBatch(List<String> ids, User operatorUser);

    /**
     * 列表分页查询路线介绍图
     *
     * @param predicateWrapper 条件谓语包装类
     * @param pageable         分页参数
     * @return 统一结果集
     */
    R<List<RouteImageDTO>> queryRouteImageListByPage(PredicateWrapper predicateWrapper, Pageable pageable);

    /**
     * 根据ID查询路线介绍图
     *
     * @param id ID
     * @return 编辑DTO
     */
    RouteImageEditDTO queryRouteImageById(String id);


    /**
     * 导出路线介绍图Excel
     *
     * @param predicateWrapper 查询条件包装类
     * @return 数据集合
     */
    List<RouteImageDTO> export(PredicateWrapper predicateWrapper);

    // ======================================== 以上由代码生成器生成 ======================================== //

    /**
     * 根据predicate查询
     *
     * @param predicate 查询条件
     * @return 数据集合
     */
    List<RouteImageEditDTO> queryRouteImageEditDTOListByPredicate(Predicate predicate);

}
