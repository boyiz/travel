package com.xunye.route.web;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson2.JSON;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.querydsl.core.types.Predicate;
import com.xunye.auth.entity.User;
import com.xunye.auth.tool.SecurityTools;
import com.xunye.common.constant.RouteConstant;
import com.xunye.core.base.BaseController;
import com.xunye.core.model.PredicateWrapper;
import com.xunye.core.result.R;
import com.xunye.core.tools.CheckTools;
import com.xunye.core.tools.RedisUtil;
import com.xunye.route.dto.RouteInfoDTO;
import com.xunye.route.dto.RouteInfoEditDTO;
import com.xunye.route.entity.RouteInfo;
import com.xunye.route.service.IRouteInfoService;
import com.xunye.route.vo.RouteDetailVo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/route")
public class RouteInfoController extends BaseController {

    @Resource
    private IRouteInfoService routeInfoService;
    @Resource
    private SecurityTools securityTools;
    @Resource
    private RedisUtil redisUtil;

    /**
     * 根据ID查询路线接口
     *
     * @param id ID
     */
    @GetMapping("/{id}")
    public R<RouteDetailVo> queryRouteInfoById(@PathVariable String id) {
        // redis 来一份
        Object redisObj = redisUtil.get(RouteConstant.ROUTE_PREX + id);
        if (CheckTools.isNotNullOrEmpty(redisObj)) {
            return R.success("查询成功",JSON.parseObject(redisObj.toString(), RouteDetailVo.class));
        }
        RouteDetailVo routeDetailVo = routeInfoService.queryRouteInfoByRouteId(id);
        return R.success("查询成功", routeDetailVo);
    }

    //@GetMapping("/{routeId}")
    //public R<RouteDetailVo> queryByRouteId(@PathVariable String routeId, @QuerydslPredicate(root = RouteInfo.class) Predicate predicate) {
    //    if (CheckTools.isNullOrEmpty(routeId)) {
    //        return R.failure("路线ID不能为空");
    //    }
    //    QRouteInfo qRouteInfo = QRouteInfo.routeInfo;
    //    predicate = ExpressionUtils.and(predicate, qRouteInfo.id.eq(routeId));
    //    return routeInfoService.queryRouteInfoByRouteId(PredicateWrapper.of(predicate));
    //}

    /**
     * 路线列表查询接口
     *
     * @param predicate 条件谓语
     * @param pageable 分页参数
     */
    @GetMapping("/all")
    //@SaCheckLogin
    public R<List<RouteDetailVo>> list(@QuerydslPredicate(root = RouteInfo.class) Predicate predicate, Pageable pageable) {
        List<RouteDetailVo> routeDetailVoList = routeInfoService.queryRouteInfoListByPage(
            PredicateWrapper.of(predicate), pageable);
        return R.success("查询成功", routeDetailVoList).setTotal(routeDetailVoList.size());
    }

    /**
     * 路线创建接口
     *
     * @param addDTO 创建信息
     */
    @PostMapping
    @SaCheckRole("admin")
    public R<String> create(@RequestBody RouteInfoEditDTO addDTO) {
        addDTO.check();
        User currentRealUser = securityTools.getLoginUser(StpUtil.getLoginIdAsString());

        // 创建并返回dbId
        String dbId = routeInfoService.createRouteInfo(addDTO, currentRealUser);
        return R.success("创建成功", dbId);
    }

    /**
     * 路线更新接口
     *
     * @param editDto 更新信息
     */
    @PutMapping
    @SaCheckRole("admin")
    public R<Boolean> update(@RequestBody RouteInfoEditDTO editDto) {
        // 基础校验
        if (CheckTools.isNullOrEmpty(editDto.getId())) {
            return R.failure("路线id不存在");
        }

        User currentRealUser = securityTools.getLoginUser(StpUtil.getLoginIdAsString());

        // 更新路线
        routeInfoService.updateRouteInfo(editDto, currentRealUser);
        return R.success("更新成功", true);
    }

    /**
     * 路线删除接口
     *
     * @param id ID
     */
    @DeleteMapping("/{id}")
    @SaCheckRole("admin")
    public R<Boolean> delete(@PathVariable String id) {
        // 获取当前操作用户
        User currentRealUser = securityTools.getLoginUser(StpUtil.getLoginIdAsString());
        // 调用service删除
        routeInfoService.deleteRouteInfo(id, currentRealUser);
        return R.success("删除成功", true);
    }

    /**
     * 路线批量删除接口
     *
     * @param ids id集合
     */
    @DeleteMapping("/batch/{ids}")
    @SaCheckRole("admin")
    public R<Boolean> deleteBatch(@PathVariable String ids) {
        // 参数预处理
        if (StrUtil.isEmptyIfStr(ids)) {return R.failure();}

        // 获取当前操作用户
        User currentRealUser = securityTools.getLoginUser(StpUtil.getLoginIdAsString());
        // 调用service删除
        List<String> idList = Arrays.stream(ids.split(",")).collect(Collectors.toList());
        routeInfoService.deleteRouteInfoBatch(idList, currentRealUser);

        return R.success("批量删除成功", true);
    }


    /**
     * 路线数据导出接口
     *
     * @param predicate 条件谓语
     * @param response 相应
     * @throws IOException IO异常
     */
    @GetMapping("/export")
    @SaCheckRole("admin")
    public void export(@QuerydslPredicate(root = RouteInfo.class) Predicate predicate, HttpServletResponse response)
        throws IOException {
        String fileName = "路线列表导出.xls";
        exportExcelBefore(response, fileName);
        EasyExcel.write(response.getOutputStream(), RouteInfoDTO.class)
            .sheet("Sheet1")
            .doWrite(routeInfoService.export(PredicateWrapper.of(predicate)));
    }

}
