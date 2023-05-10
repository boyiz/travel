package com.xunye.auth.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.xunye.auth.entity.User;
import com.xunye.core.support.IHierarchical;
import lombok.Data;

@Data
public class DeptUserMixTreeNodeVo implements Serializable, IHierarchical<DeptUserMixTreeNodeVo> {

    /**
     * basic info
     */
    private String id;
    private String label;
    private String value;
    private Integer type;

    /**
     * tree relation
     */
    private Integer sortNo;
    private String parentId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<DeptUserMixTreeNodeVo> children = new ArrayList<>();

    private List<User> userList = new ArrayList<>();

    private Boolean disabled = false;

    private List<String> leaderUserList = new ArrayList<>();

    private List<String> pathList = new ArrayList<>();

    @Override
    public Integer fetchSortNo() {
        return sortNo;
    }
}
