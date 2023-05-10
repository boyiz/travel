package com.xunye.route.web;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;

import com.alibaba.excel.EasyExcel;

import com.xunye.auth.tool.SecurityTools;
import com.xunye.core.result.R;
import com.xunye.core.base.BaseController;
import com.xunye.core.model.PredicateWrapper;
import com.xunye.route.dto.RouteScheduleDTO;
import com.xunye.route.dto.RouteScheduleEditDTO;
import com.xunye.route.entity.RouteSchedule;
import com.xunye.route.service.IRouteScheduleService;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.web.bind.annotation.*;

import com.xunye.auth.entity.User;
import com.xunye.core.tools.CheckTools;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/route/schedule")
@SaCheckRole("admin")
public class RouteScheduleController extends BaseController {

    @Resource
    private IRouteScheduleService routeScheduleService;
    @Resource
    private SecurityTools securityTools;

    /**
     * 路线-每天计划列表查询接口
     *
     * @param predicate 条件谓语
     * @param pageable 分页参数
     */
    @GetMapping
    public R<List<RouteScheduleDTO>> list(@QuerydslPredicate(root = RouteSchedule.class) Predicate predicate,
        Pageable pageable) {
        return routeScheduleService.queryRouteScheduleListByPage(PredicateWrapper.of(predicate), pageable);
    }

    /**
     * 路线-每天计划创建接口
     *
     * @param addDTO 创建信息
     */
    @PostMapping
    public R<String> create(@RequestBody RouteScheduleEditDTO addDTO) {
        addDTO.check();
        User currentRealUser = securityTools.getLoginUser(StpUtil.getLoginIdAsString());

        // 创建并返回dbId
        String dbId = routeScheduleService.createRouteSchedule(addDTO, currentRealUser);
        return R.success("创建成功", dbId);
    }

    /**
     * 路线-每天计划更新接口
     *
     * @param editDto 更新信息
     */
    @PutMapping
    public R<Boolean> update(@RequestBody RouteScheduleEditDTO editDto) {
        // 基础校验
        if (CheckTools.isNullOrEmpty(editDto.getId())) {
            return R.failure("路线-每天计划id不存在");
        }

        User currentRealUser = securityTools.getLoginUser(StpUtil.getLoginIdAsString());

        // 更新路线-每天计划
        routeScheduleService.updateRouteSchedule(editDto, currentRealUser);
        return R.success("更新成功", true);
    }

    /**
     * 路线-每天计划删除接口
     *
     * @param id ID
     */
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable String id) {
        // 获取当前操作用户
        User currentRealUser = securityTools.getLoginUser(StpUtil.getLoginIdAsString());
        // 调用service删除
        routeScheduleService.deleteRouteSchedule(id, currentRealUser);
        return R.success("删除成功", true);
    }

    /**
     * 路线-每天计划批量删除接口
     *
     * @param ids id集合
     */
    @DeleteMapping("/batch/{ids}")
    public R<Boolean> deleteBatch(@PathVariable String ids) {
        // 参数预处理
        if (StrUtil.isEmptyIfStr(ids)) {return R.failure();}

        // 获取当前操作用户
        User currentRealUser = securityTools.getLoginUser(StpUtil.getLoginIdAsString());
        // 调用service删除
        List<String> idList = Arrays.stream(ids.split(",")).collect(Collectors.toList());
        routeScheduleService.deleteRouteScheduleBatch(idList, currentRealUser);

        return R.success("批量删除成功", true);
    }

    /**
     * 根据RouteID查询路线-每天计划接口
     *
     * @param routeId 路线ID
     */
    @GetMapping("/{routeId}")
    public R<List<RouteScheduleEditDTO>> queryRouteScheduleById(@PathVariable String routeId) {
        List<RouteScheduleEditDTO> routeScheduleEditDTOS = routeScheduleService.queryRouteScheduleByRouteId(routeId);
        return R.success("查询成功", routeScheduleEditDTOS).setTotal(routeScheduleEditDTOS.size());
    }

    /**
     * 路线-每天计划数据导出接口
     *
     * @param predicate 条件谓语
     * @param response 相应
     * @throws IOException IO异常
     */
    @GetMapping("/export")
    public void export(@QuerydslPredicate(root = RouteSchedule.class) Predicate predicate, HttpServletResponse response)
        throws IOException {
        String fileName = "路线-每天计划列表导出.xls";
        exportExcelBefore(response, fileName);
        EasyExcel.write(response.getOutputStream(), RouteScheduleDTO.class)
            .sheet("Sheet1")
            .doWrite(routeScheduleService.export(PredicateWrapper.of(predicate)));
    }

    /**
     * 路线-每天计划表单数据初始化
     *
     * @return Map
     */
    //@GetMapping("/initRouteScheduleFormData")
    //public R<Map<String, Object>> initRouteScheduleFormData() {
    //    Map<String, Object> resultMap = new HashMap<>();
    //    // TODO: 数据填充
    //    return R.success(resultMap);
    //}

}
