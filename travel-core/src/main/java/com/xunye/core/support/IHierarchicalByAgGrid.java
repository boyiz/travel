package com.xunye.core.support;

import java.util.List;

/**
 * 1. 具有层次结构的接口，用于抽象AgGrid_Tree的数据结构
 */
public interface IHierarchicalByAgGrid<T>{

    /**
     * 获取对象ID
     */
    String getObjectId();

    /**
     * 获取节点的父对象ID
     */
    String getParentId();

    /**
     * 树节点路径
     */
    List<String> getDataPath();

    /**
     * 设置树节点路径
     */
    void setDataPath(List<String> dataPath);


    /**
     * 排序用
     */
    Integer getSortNo();

    /**
     * 排序用
     */
    void setSortNo(Integer sortNo);

}
