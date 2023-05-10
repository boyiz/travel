package com.xunye.core.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.xunye.core.support.IHierarchical;
import com.xunye.core.support.IHierarchicalByAgGrid;
import com.xunye.core.tools.CheckTools;

public class TreeHelper {

    /**
     * @param dataList 需要转化为Tree的集合
     * @param <E>      {@link IHierarchical}接口的实现类
     * @return TreeList
     */
    public static <E extends IHierarchical<E>> List<E> toTree(List<E> dataList) {
        int[] roots = new int[dataList.size()];
        Arrays.fill(roots, 0);
        for (int i = 0; i < dataList.size(); i++) {
            E itemI = dataList.get(i);
            List<E> children = new ArrayList<>();
            for (int j = 0; j < dataList.size(); j++) {
                if (i == j) {
                    continue;
                }
                E itemJ = dataList.get(j);
                if (Objects.equals(itemJ.getParentId(), itemI.getId())) {
                    children.add(itemJ);
                    roots[j] = 1;
                }
            }
            if (children.size() > 0) {
                SortHelper.sort(children);
            }
            itemI.setChildren(children.size() > 0 ? children : null);
        }
        List<E> resultList = new ArrayList<>();
        for (int i = 0; i < roots.length; i++) {
            if (roots[i] == 0) {
                resultList.add(dataList.get(i));
            }
        }
        if (resultList.size() > 0) {
            SortHelper.sort(resultList);
        }
        return resultList;
    }


    /**
     * 递归查找后代列表（非树结构）
     *
     * @param dataList   数据列表集合
     * @param targetList 目标后代集合
     * @param parentId   父ID
     * @param <E>        IHierarchical<E>
     */
    public static <E extends IHierarchical<E>> void findOffspringList(List<E> dataList, List<E> targetList, String parentId) {
        dataList.forEach(item -> {
            if (Objects.equals(parentId, item.getParentId())) {
                targetList.add(item);
                findOffspringList(dataList, targetList, item.getId());
            }
        });
    }


    public static <E extends IHierarchicalByAgGrid<E>> List<E> toTreeByAgGrid(List<E> dataList) {
        Map<String, List<E>> groupById = dataList.stream().collect(Collectors.groupingBy(E::getObjectId));

        for (E itemI : dataList) {
            List<String> dataPath = new ArrayList<>();
            dataPath.add(itemI.getObjectId());

            String parentId = itemI.getParentId();
            boolean flag = true;
            do {
                if (CheckTools.isNotNullOrEmpty(parentId)) {
                    List<E> list = groupById.get(parentId);
                    if (CheckTools.isNotNullOrEmpty(list)) {
                        E es = list.get(list.size() - 1);
                        if (CheckTools.isNotNullOrEmpty(es)) {
                            dataPath.add(es.getObjectId());
                            parentId = es.getParentId();
                        } else {
                            flag = false;
                        }
                    } else {
                        flag = false;
                    }
                } else {
                    flag = false;
                }
            } while (flag);

            Collections.reverse(dataPath);
            itemI.setDataPath(dataPath);

            itemI.setSortNo(dataPath.size());
        }
        dataList.sort(Comparator.comparing(E::getSortNo, Comparator.nullsLast(Integer::compareTo)));

        return dataList;
    }

    // 测试代码
    // @Data
    // @AllArgsConstructor
    // public static class TreeNode implements IHierarchical {
    //
    //     private Long id;
    //
    //     private Long pId;
    //
    //     private List<TreeNode> children;
    //
    //     private Integer sort;
    //
    //     @Override
    //     public Long getId() {
    //         return id;
    //     }
    //
    //     @Override
    //     public Long getParentId() {
    //         return pId;
    //     }
    //
    //     @Override
    //     public List getChildren() {
    //         return children;
    //     }
    //
    //     @Override
    //     public void setChildren(List children) {
    //         this.children = children;
    //     }
    //
    //     @Override
    //     public Integer fetchSortNo() {
    //         return sort;
    //     }
    // }
    //
    // // 测试递归查找
    // public static void main(String[] args) {
    //     // 一级
    //     TreeNode t1 = new TreeNode(1L, 0L, null, 0);
    //     TreeNode t2 = new TreeNode(2L, 0L, null, 1);
    //     TreeNode t3 = new TreeNode(3L, 0L, null, 2);
    //
    //     // 二级
    //     TreeNode t11 = new TreeNode(4L, 1L, null, 0);
    //     TreeNode t12 = new TreeNode(5L, 1L, null, 1);
    //
    //     TreeNode t31 = new TreeNode(6L, 3L, null, 0);
    //
    //     // 三级
    //     TreeNode t111 = new TreeNode(7L, 4L, null, 0);
    //
    //     // 原始列表
    //     List<TreeNode> dataList = Lists.newArrayList(t1, t2, t3, t11, t12, t31, t111);
    //     // String toJson = GsonTools.getGson().toJson(dataList);
    //     // System.out.println(toJson);
    //
    //     // 转树形结构
    //     // List<TreeNode> treeNodeList = toTree(dataList);
    //     // String toJson1 = GsonTools.getGson().toJson(treeNodeList);
    //     // System.out.println(toJson1);
    //
    //     // 从list中找出符合条件的子list
    //     List<TreeNode> targetList = Lists.newArrayList();
    //     findOffspringList(dataList, targetList, 1L);
    //     String toJson = GsonTools.getGson().toJson(targetList);
    //     System.out.println(toJson);
    //
    //     List<TreeNode> qwe = toTree(targetList);
    //     String qqq = GsonTools.getGson().toJson(qwe);
    //     System.out.println(qqq);
    //
    // }

}
