package com.xunye.dispatch.biz.impl;

import java.util.Date;

import com.xunye.auth.entity.User;
import com.xunye.dispatch.biz.GroupStockHandlerBiz;
import com.xunye.group.dto.GroupInfoEditDTO;
import com.xunye.group.service.IGroupInfoService;
import com.xunye.order.event.GroupStockUpdateEvent;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @ClassName GroupStockHandlerBizImpl
 * @Description
 * @Author boyiz
 * @Date 2023/5/9 11:37
 * @Version 1.0
 **/
public class GroupStockHandlerBizImpl implements GroupStockHandlerBiz {

    @Autowired
    private IGroupInfoService groupInfoService;

    @Override
    public void onGroupStockUpdate(GroupStockUpdateEvent event) {
        String groupId = event.getGroupId();
        int number = event.getNumber();
        GroupInfoEditDTO groupInfoEditDTO = groupInfoService.queryGroupInfoById(groupId);
        groupInfoEditDTO.setActualCount(groupInfoEditDTO.getActualCount() + number);
        User user = new User();
        user.setCreateByName("调度中心:库存事件监听");
        groupInfoService.updateGroupInfo(groupInfoEditDTO,user);
    }
}
