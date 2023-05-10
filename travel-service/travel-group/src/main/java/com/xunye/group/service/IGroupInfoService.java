package com.xunye.group.service;

import com.xunye.group.dto.GroupInfoDTO;
import com.xunye.group.dto.GroupInfoEditDTO;
import com.xunye.group.entity.GroupInfo;
import com.xunye.core.base.BaseService;
import com.xunye.core.model.PredicateWrapper;
import com.xunye.core.result.R;
import org.springframework.data.domain.Pageable;

import com.xunye.auth.entity.User;
import com.querydsl.core.types.Predicate;

import java.util.List;

/**
 * groupService接口
 *
 * @Author: boyiz
 * @Date: 2023-04-13
 */
public interface IGroupInfoService extends BaseService<GroupInfoEditDTO, GroupInfoDTO, GroupInfo,String> {

    /**
     * 创建group
     *
     * @param createDto 编辑DTO
     * @param operatorUser 当前操作用户
     * @return ID
     */
    String createGroupInfo(GroupInfoEditDTO createDto, User operatorUser);

    /**
     * 更新group
     *
     * @param updateDto 编辑DTO
     * @param operatorUser 当前操作用户
     */
    void updateGroupInfo(GroupInfoEditDTO updateDto, User operatorUser);

    /**
     * 根据ID删除group
     *
     * @param id groupID
     */
    void deleteGroupInfo(String id, User operatorUser);

    /**
     * 批量删除group
     *
     * @param ids groupID集合
     */
    void deleteGroupInfoBatch(List<String> ids, User operatorUser);

    /**
     * 列表分页查询group
     *
     * @param predicateWrapper 条件谓语包装类
     * @param pageable         分页参数
     * @return 统一结果集
     */
    R<List<GroupInfoDTO>> queryGroupInfoListByPage(PredicateWrapper predicateWrapper, Pageable pageable);

    /**
     * 根据ID查询group
     *
     * @param id ID
     * @return 编辑DTO
     */
    GroupInfoEditDTO queryGroupInfoById(String id);


    /**
     * 导出groupExcel
     *
     * @param predicateWrapper 查询条件包装类
     * @return 数据集合
     */
    List<GroupInfoDTO> export(PredicateWrapper predicateWrapper);

    // ======================================== 以上由代码生成器生成 ======================================== //

    /**
     * 根据predicate查询
     *
     * @param predicate 查询条件
     * @return 数据集合
     */
    List<GroupInfoEditDTO> queryGroupInfoEditDTOListByPredicate(Predicate predicate);

}
