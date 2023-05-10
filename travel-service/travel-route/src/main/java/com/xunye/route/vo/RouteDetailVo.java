package com.xunye.route.vo;

import java.util.List;

import com.xunye.group.dto.GroupInfoDTO;
import com.xunye.notice.dto.GeneralNoticeDTO;
import com.xunye.notice.dto.RouteNoticeDTO;
import com.xunye.route.dto.RouteImageDTO;
import com.xunye.route.dto.RouteInfoDTO;
import com.xunye.route.dto.RouteScheduleDTO;
import lombok.Data;

/**
 * @ClassName RouteDetailVo
 * @Description 旅行路线详细信息
 * @Author boyiz
 * @Date 2023/4/18 16:26
 * @Version 1.0
 **/
@Data
public class RouteDetailVo {

    private RouteInfoDTO routeInfo;
    private List<RouteScheduleDTO> routeScheduleList;

    private GeneralNoticeDTO generalNotice;
    private RouteNoticeDTO routeNotice;

    private List<GroupInfoDTO> groupInfo;

    private List<RouteImageDTO> routeImageList;

}
