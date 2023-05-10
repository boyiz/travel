package com.xunye.auth.biz;

import java.util.List;

import com.xunye.auth.entity.User;
import com.xunye.auth.vo.DeptUserMixTreeNodeVo;

public interface IDeptBiz {

    /**
     * 查询【多选】部门用户混合树 列表
     * 主体是用户的选择
     */
    List<DeptUserMixTreeNodeVo> queryMultipleDeptUserMixTreeList(String deptIds, User operator);

    /**
     * 查询【单选】部门用户混合树 列表
     * 主体是部门的选择
     */
    List<DeptUserMixTreeNodeVo> queryRadioDeptUserMixTreeList(User operator, Integer isBizDept);


}
