package com.xunye.core.support;

import java.util.List;

/**
 * 1. 具有层次结构的接口，用于抽象所有类似Tree的数据结构
 * 2. 默认继承{@link ISortable}接口
 */
public interface IHierarchical<T> extends ISortable {

    /**
     * 获取节点ID
     */
    String getId();

    /**
     * 获取节点的父ID
     */
    String getParentId();

    /**
     * 获取所有子节点
     */
    List<T> getChildren();

    /**
     * 设置子节点
     */
    void setChildren(List<T> children);

}
