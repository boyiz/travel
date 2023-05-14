package com.xunye.auth.biz.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.xunye.auth.vo.DeptUserMixTreeNodeVo;
import com.xunye.core.exception.BusinessException;
import com.xunye.core.helper.TreeHelper;
import com.xunye.core.tools.CheckTools;
import com.xunye.core.tools.StringTools;
import com.xunye.auth.biz.IDeptBiz;
import com.xunye.auth.entity.Dept;
import com.xunye.auth.entity.QDept;
import com.xunye.auth.entity.User;
import com.xunye.auth.service.IDeptService;
import com.xunye.auth.service.ISysUserService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class DeptBizImpl implements IDeptBiz {

    private static final Integer MERGE_STRATEGY_USER_MULTIPLE = 1;
    private static final Integer MERGE_STRATEGY_DEPT_RADIO = 2;

    @Autowired
    private IDeptService deptService;
    @Autowired
    private ISysUserService userService;


    // 部门 => mixTreeNode
    private DeptUserMixTreeNodeVo deptToMixTreeNodeMapping(Dept dept) {
        DeptUserMixTreeNodeVo mixTreeNode = new DeptUserMixTreeNodeVo();
        mixTreeNode.setId(dept.getId());
        mixTreeNode.setLabel(dept.getDeptName());
        mixTreeNode.setValue(dept.getId());
        mixTreeNode.setParentId(dept.getParentId());
        // 部门(11)、用户(12)
        mixTreeNode.setType(11);
        mixTreeNode.setSortNo(dept.getSortNo());
        // 负责人
        String leaderUserIds = dept.getLeaderUserIds();
        if (CheckTools.isNotNullOrEmpty(leaderUserIds)) {
            List<String> leaderUserIdList = StringTools.split(leaderUserIds, ",");
            mixTreeNode.setLeaderUserList(leaderUserIdList);
        } else {
            mixTreeNode.setLeaderUserList(new ArrayList<>());
        }
        return mixTreeNode;
    }

    // 用户 => mixNode
    private DeptUserMixTreeNodeVo userToMixTreeNodeMapping(User user) {
        DeptUserMixTreeNodeVo userMixTreeNode = new DeptUserMixTreeNodeVo();
        userMixTreeNode.setId(user.getId());
        userMixTreeNode.setLabel(user.getUserName());
        userMixTreeNode.setValue(user.getId());
        userMixTreeNode.setParentId(user.getDeptId());
        userMixTreeNode.setType(12);
        return userMixTreeNode;
    }

    /* 构建原始部门树 */
    private List<DeptUserMixTreeNodeVo> buildOriginalDeptTreeAction(List<Dept> deptList) {
        // 数据结构mapping
        List<DeptUserMixTreeNodeVo> mixTreeNodeList = deptList.stream().map(this::deptToMixTreeNodeMapping).collect(Collectors.toList());
        // 构建树
        return TreeHelper.toTree(mixTreeNodeList);
    }

    // 内存搜索，本部门的所有用户
    private List<User> searchThisDeptUserList(List<User> allUserList, String thisDeptId) {
        return allUserList.stream().filter(user -> {
            if (CheckTools.isNotNullOrEmpty(user.getDeptId()) && user.getDeptId().equals(thisDeptId)) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());
    }

    /* 挂载用户到部门树，构建为混合树 */
    private void appendUserRebuildMixTreeAction(List<DeptUserMixTreeNodeVo> mixTreeNodeList, List<User> allUserList) {
        for (DeptUserMixTreeNodeVo mixTreeNode : mixTreeNodeList) {
            // 仅处理部门节点
            if (!mixTreeNode.getType().equals(11)) {
                continue;
            }

            // 1.深度优先挂载
            if (CheckTools.isNotNullOrEmpty(mixTreeNode.getChildren())) {
                appendUserRebuildMixTreeAction(mixTreeNode.getChildren(), allUserList);
            } else {
                mixTreeNode.setChildren(new ArrayList<>());
            }
            List<DeptUserMixTreeNodeVo> subMixTreeList = mixTreeNode.getChildren();

            // 2.实际挂载当前节点
            List<User> curDeptNodeUserList = searchThisDeptUserList(allUserList, mixTreeNode.getId());
            // 暂时不缓存原始userList
//            mixTreeNode.setUserList(curDeptNodeUserList);
            List<DeptUserMixTreeNodeVo> userMixTreeNodeList = curDeptNodeUserList.stream().map(this::userToMixTreeNodeMapping).collect(Collectors.toList());
            // 将用户作为该部门的子节点，挂载
            subMixTreeList.addAll(userMixTreeNodeList);
        }
    }

    /* 生成树node路径，方便后期搜索/消费 */
    private void generateTreeNodePathAction(List<DeptUserMixTreeNodeVo> mixTreeNodeList, List<String> pathList) {
        if (CheckTools.isNullOrEmpty(mixTreeNodeList)) {
            return;
        }

        for (DeptUserMixTreeNodeVo mixTreeNode : mixTreeNodeList) {
            List<String> curNodePathList = mixTreeNode.getPathList();
            if (CheckTools.isNotNullOrEmpty(pathList)) {
                curNodePathList.addAll(pathList);
            }
            // 生成下一层的路径
            List<String> nextPathList = Lists.newArrayList(pathList);
            nextPathList.add(mixTreeNode.getId());
            // 递归子树处理
            generateTreeNodePathAction(mixTreeNode.getChildren(), nextPathList);
        }
    }

    // 判断该节点及其子树，是否属于负责的部门
    private boolean searchCurNodeAndSubTreeIfBelongOwner(DeptUserMixTreeNodeVo mixTreeNode, List<Dept> ownerDeptList) {
        boolean flag = false;
        for (Dept dept : ownerDeptList) {
            // 当前节点是否属于负责部门
            if (mixTreeNode.getId().equals(dept.getId())) {
                flag = true;
                break;
            }

            // 当前节点的path(祖先)是否属于负责部门
            boolean pathSearchFlag = searchTreeNodePath(mixTreeNode, dept.getId());
            if (pathSearchFlag) {
                flag = true;
                break;
            }

            // 搜索子树，是否包含负责的部门
            boolean subTreeSearchFlag = false;
            List<DeptUserMixTreeNodeVo> subMixTreeList = mixTreeNode.getChildren();
            for (DeptUserMixTreeNodeVo subMixTreeNode : subMixTreeList) {
                subTreeSearchFlag = searchCurNodeAndSubTreeIfBelongOwner(subMixTreeNode, ownerDeptList);
                if (subTreeSearchFlag) {
                    break;
                }
            }
            if (subTreeSearchFlag) {
                flag = true;
                break;
            }
        }
        return flag;
    }


    // 处理部门节点
    private void processDeptTreeNode(DeptUserMixTreeNodeVo mixTreeNode, List<Dept> ownerDeptList) {
        // 1.获取子树
        List<DeptUserMixTreeNodeVo> subMixTreeList = mixTreeNode.getChildren();
        // 2.存在子树，递归处理
        if (CheckTools.isNotNullOrEmpty(subMixTreeList)) {
            boolean searchResult = searchCurNodeAndSubTreeIfBelongOwner(mixTreeNode, ownerDeptList);
            // 如果搜索到了，说明该节点下存在负责的部门，所以该节点需要放行
            mixTreeNode.setDisabled(!searchResult);
        }
        // 3.不存在，直接禁用
        else {
            mixTreeNode.setDisabled(true);
        }
    }

    // 从节点的路径中搜索
    private boolean searchTreeNodePath(DeptUserMixTreeNodeVo mixTreeNode, String ownerDeptId) {
        boolean pathSearchFlag = false;
        for (String pId : mixTreeNode.getPathList()) {
            if (CheckTools.isNotNullOrEmpty(pId) && pId.equals(ownerDeptId)) {
                pathSearchFlag = true;
                break;
            }
        }
        return pathSearchFlag;
    }

    // 处理用户节点
    private void processUserTreeNode(DeptUserMixTreeNodeVo mixTreeNode, List<Dept> ownerDeptList) {
        // 1.获取子树
        List<DeptUserMixTreeNodeVo> subMixTreeList = mixTreeNode.getChildren();
        // 2.用户节点存在子树，直接抛异常
        if (CheckTools.isNotNullOrEmpty(subMixTreeList)) {
            throw new BusinessException("用户节点存在子树");
        }
        // 3.实际处理
        else {
            boolean searchResult = false;
            for (Dept dept : ownerDeptList) {
                searchResult = searchTreeNodePath(mixTreeNode, dept.getId());
                if (searchResult) {
                    break;
                }
            }
            mixTreeNode.setDisabled(!searchResult);
        }
    }


    // 合并策略【用户多选】
    private void mergeMixTreeNodeStrategyUserMultiple(DeptUserMixTreeNodeVo mixTreeNode, List<Dept> ownerDeptList) {
        // 1.部门节点处理
        if (mixTreeNode.getType().equals(11)) {
            processDeptTreeNode(mixTreeNode, ownerDeptList);
        }
        // 2.用户节点处理
        else if (mixTreeNode.getType().equals(12)) {
            processUserTreeNode(mixTreeNode, ownerDeptList);
        }
        // 3.异常情况
        else {
            throw new BusinessException("树上存在未知节点");
        }
    }

    // 合并策略【部门单选】
    private void mergeMixTreeNodeStrategyDeptRadio(DeptUserMixTreeNodeVo mixTreeNode, List<Dept> ownerDeptList) {
        if (!mixTreeNode.getType().equals(11)) {
            throw new BusinessException("存在非部门节点");
        }

        // 仅需判断当前节点，是否是负责的部门
        boolean flag = false;
        for (Dept dept : ownerDeptList) {
            // 判断节点本身
            if (dept.getId().equals(mixTreeNode.getId())) {
                flag = true;
                break;
            }
            // 判断节点祖先
            for (String pId : mixTreeNode.getPathList()) {
                if (CheckTools.isNotNullOrEmpty(pId) && pId.equals(dept.getId())) {
                    flag = true;
                    break;
                }
            }
        }
        mixTreeNode.setDisabled(!flag);
    }

    /* 合并权限信息到mixTree */
    // 合并策略选择：1【用户多选】、2【部门单选】
    private void mergeMixTreeByOwnerDeptListAction(List<DeptUserMixTreeNodeVo> mixTreeNodes, List<Dept> ownerDeptList, int mergeStrategy) {
        if (CheckTools.isNullOrEmpty(mixTreeNodes)) {
            return;
        }
        for (DeptUserMixTreeNodeVo mixTreeNode : mixTreeNodes) {
            // 处理当前节点的合并
            if (mergeStrategy == MERGE_STRATEGY_USER_MULTIPLE) {
                mergeMixTreeNodeStrategyUserMultiple(mixTreeNode, ownerDeptList);
            } else if (mergeStrategy == MERGE_STRATEGY_DEPT_RADIO) {
                mergeMixTreeNodeStrategyDeptRadio(mixTreeNode, ownerDeptList);
            } else {
                throw new BusinessException("未知的合并策略");
            }

            // 处理该节点的子树
            mergeMixTreeByOwnerDeptListAction(mixTreeNode.getChildren(), ownerDeptList, mergeStrategy);
        }
    }

    @Override
    public List<DeptUserMixTreeNodeVo> queryMultipleDeptUserMixTreeList(String deptIds, User operator) {
        /**
         * 0、构造条件
         */
        Predicate predicate = new BooleanBuilder();
        if (CheckTools.isNotNullOrEmpty(deptIds)) {
            String[] split = deptIds.split(",");
            QDept qDept = QDept.dept;
            predicate = qDept.id.in(split);
        }

        /**
         * 1、构造部门tree
         */
        List<Dept> allDeptList = deptService.queryDeptListByPredicate(predicate);
        List<DeptUserMixTreeNodeVo> mixTreeNodeList = buildOriginalDeptTreeAction(allDeptList);

        /**
         * 2、向部门tree挂载用户，形成混合树
         */
        List<User> allUserList = userService.getRepository().findAll();
        appendUserRebuildMixTreeAction(mixTreeNodeList, allUserList);

        /**
         * 3、为tree上的每个node生成path
         */
        generateTreeNodePathAction(mixTreeNodeList, Lists.newArrayList("root"));

        /**
         * 4、根据操作者的权限，对tree做merge
         */
        if (operator.judgeIfSuperAdmin()) {
            mergeMixTreeByOwnerDeptListAction(mixTreeNodeList, allDeptList, MERGE_STRATEGY_USER_MULTIPLE);
        } else {
            // 查询负责的部门集合
            List<Dept> thisUserResponsibleDeptList = deptService.queryResponsibleDeptListByUser(operator.getId());
            // 根据负责的部门，merge到mixTree中
            mergeMixTreeByOwnerDeptListAction(mixTreeNodeList, thisUserResponsibleDeptList, MERGE_STRATEGY_USER_MULTIPLE);
        }
        return mixTreeNodeList;
    }

    @Override
    public List<DeptUserMixTreeNodeVo> queryRadioDeptUserMixTreeList(User operator, Integer isBizDept) {
        /**
         * 1、构造部门tree
         */
        QDept qDept = QDept.dept;
        Predicate predicate = new BooleanBuilder();
        if (CheckTools.isNotNullOrEmpty(isBizDept)) {
            predicate = ExpressionUtils.and(predicate, qDept.isBizDept.eq(isBizDept));
        }
        List<Dept> allDeptList = deptService.queryDeptListByPredicate(predicate);
        List<DeptUserMixTreeNodeVo> mixTreeNodeList = buildOriginalDeptTreeAction(allDeptList);

        /**
         * 2、为tree上的每个node生成path
         */
        generateTreeNodePathAction(mixTreeNodeList, Lists.newArrayList("root"));

        /**
         * 3、根据操作者的权限，对tree做merge
         */
        if (operator.judgeIfSuperAdmin()) {
            mergeMixTreeByOwnerDeptListAction(mixTreeNodeList, allDeptList, MERGE_STRATEGY_DEPT_RADIO);
        } else {
            List<Dept> thisUserResponsibleDeptList = deptService.queryResponsibleDeptListByUser(operator.getId());
            mergeMixTreeByOwnerDeptListAction(mixTreeNodeList, thisUserResponsibleDeptList, MERGE_STRATEGY_DEPT_RADIO);
        }
        return mixTreeNodeList;
    }

}
