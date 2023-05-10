package com.xunye.route.web;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;

import com.xunye.auth.tool.SecurityTools;
import com.xunye.core.result.R;
import com.xunye.core.base.BaseController;
import com.xunye.core.model.PredicateWrapper;
import com.xunye.route.dto.RouteImageDTO;
import com.xunye.route.dto.RouteImageEditDTO;
import com.xunye.route.entity.RouteImage;
import com.xunye.route.service.IRouteImageService;
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
@RequestMapping("/route/image")
public class RouteImageController extends BaseController {

    @Resource
    private IRouteImageService routeImageService;

    @Resource
    private SecurityTools securityTools;

    /**
     * 路线介绍图列表查询接口
     *
     * @param predicate 条件谓语
     * @param pageable  分页参数
     */
    @GetMapping
    public R<List<RouteImageDTO>> list(@QuerydslPredicate(root = RouteImage.class) Predicate predicate,
                                         Pageable pageable) {
        // 禁止不带参数查询
        if (CheckTools.isNullOrEmpty(predicate)) {
            return R.failure("请求参数有误");
        }
        return routeImageService.queryRouteImageListByPage(PredicateWrapper.of(predicate), pageable);
    }

    /**
     * 路线介绍图创建接口
     *
     * @param addDTO 创建信息
     */
    @PostMapping
    @SaCheckRole("admin")
    public R<String> create(@RequestBody RouteImageEditDTO addDTO) {
        addDTO.check();

        User currentRealUser = securityTools.getLoginUser(StpUtil.getLoginIdAsString());

        // 创建并返回dbId
        String dbId = routeImageService.createRouteImage(addDTO, currentRealUser);
        return R.success("创建成功",dbId);
    }

    /**
     * 路线介绍图更新接口
     *
     * @param editDto 更新信息
     */
    @PutMapping
    @SaCheckRole("admin")
    public R<Boolean> update(@RequestBody RouteImageEditDTO editDto) {
        // 基础校验
        if (CheckTools.isNullOrEmpty(editDto.getId())) {
            return R.failure("路线介绍图id不存在" );
        }
        User currentRealUser = securityTools.getLoginUser(StpUtil.getLoginIdAsString());

        // 更新路线介绍图
        routeImageService.updateRouteImage(editDto, currentRealUser);
        return R.success("更新成功", true);
    }

    /**
     * 路线介绍图删除接口
     *
     * @param id ID
     */
    @DeleteMapping("/{id}")
    @SaCheckRole("admin")
    public R<Boolean> delete(@PathVariable String id) {
        // 获取当前操作用户
        User currentRealUser = securityTools.getLoginUser(StpUtil.getLoginIdAsString());
        // 调用service删除
        routeImageService.deleteRouteImage(id,currentRealUser);
        return R.success("删除成功", true);
    }

    /**
     * 路线介绍图批量删除接口
     *
     * @param ids id集合
     */
    @DeleteMapping("/batch/{ids}")
    @SaCheckRole("admin")
    public R<Boolean> deleteBatch(@PathVariable String ids) {
        // 参数预处理
        if (StrUtil.isEmptyIfStr(ids)) return R.failure();

        // 获取当前操作用户
        User currentRealUser = securityTools.getLoginUser(StpUtil.getLoginIdAsString());
        // 调用service删除
        List<String> idList = Arrays.stream(ids.split(",")).collect(Collectors.toList());
        routeImageService.deleteRouteImageBatch(idList,currentRealUser);

        return R.success("批量删除成功", true);
    }

    /**
     * 根据ID查询路线介绍图接口
     *
     * @param id ID
     */
    @GetMapping("/{id}")
    @SaCheckRole("admin")
    public R<RouteImageEditDTO> queryRouteImageById(@PathVariable String id) {
        RouteImageEditDTO routeImageEditDTO = routeImageService.queryRouteImageById(id);
        return R.success("查询成功",routeImageEditDTO);
    }

    /**
     * 路线介绍图数据导出接口
     *
     * @param predicate 条件谓语
     * @param response  相应
     * @throws IOException IO异常
     */
    @GetMapping("/export")
    @SaCheckRole("admin")
    public void export(@QuerydslPredicate(root = RouteImage.class) Predicate predicate, HttpServletResponse response) throws IOException {
        String fileName = "路线介绍图列表导出.xls";
        exportExcelBefore(response, fileName);
        EasyExcel.write(response.getOutputStream(), RouteImageDTO.class)
                .sheet("Sheet1")
                .doWrite(routeImageService.export(PredicateWrapper.of(predicate)));
    }

    /**
     * 路线介绍图表单数据初始化
     *
     * @return Map
     */
    //@GetMapping("/initRouteImageFormData")
    //public R<Map<String, Object>> initRouteImageFormData() {
    //    Map<String, Object> resultMap = new HashMap<>();
    //    // TODO: 数据填充
    //    return R.success(resultMap);
    //}

}
