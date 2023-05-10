package com.xunye.promotion.web;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;

import com.alibaba.excel.EasyExcel;

import com.xunye.core.result.R;
//import com.xunye.core.annotation.ApiAuth;
import com.xunye.core.base.BaseController;
import com.xunye.core.model.PredicateWrapper;
import com.xunye.promotion.dto.CouponsHistoryDTO;
import com.xunye.promotion.dto.CouponsHistoryEditDTO;
import com.xunye.promotion.entity.CouponsHistory;
import com.xunye.promotion.service.ICouponsHistoryService;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.web.bind.annotation.*;

import com.xunye.auth.entity.User;
import com.xunye.core.tools.CheckTools;
import com.xunye.auth.tool.SecurityTools;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/promotion/coupon-history")
@SaCheckRole("admin")
public class CouponsHistoryController extends BaseController {

    @Resource
    private ICouponsHistoryService couponsHistoryService;
    @Resource
    private SecurityTools securityTools;

    /**
     * 优惠券历史记录列表查询接口
     *
     * @param predicate 条件谓语
     * @param pageable 分页参数
     */
    @GetMapping
    public R<List<CouponsHistoryDTO>> list(@QuerydslPredicate(root = CouponsHistory.class) Predicate predicate,
        Pageable pageable) {
        return couponsHistoryService.queryCouponsHistoryListByPage(PredicateWrapper.of(predicate), pageable);
    }

    /**
     * 优惠券历史记录创建接口
     *
     * @param addDTO 创建信息
     */
    @PostMapping
    public R<String> create(@RequestBody CouponsHistoryEditDTO addDTO) {
        addDTO.check();
        User currentRealUser = securityTools.getLoginUser(StpUtil.getLoginIdAsString());

        // 创建并返回dbId
        String dbId = couponsHistoryService.createCouponsHistory(addDTO, currentRealUser);
        return R.success("创建成功", dbId);
    }

    /**
     * 优惠券历史记录更新接口
     *
     * @param editDto 更新信息
     */
    @PutMapping
    public R<Boolean> update(@RequestBody CouponsHistoryEditDTO editDto) {
        // 基础校验
        if (CheckTools.isNullOrEmpty(editDto.getId())) {
            return R.failure("优惠券历史记录id不存在");
        }

        User currentRealUser = securityTools.getLoginUser(StpUtil.getLoginIdAsString());

        // 更新优惠券历史记录
        couponsHistoryService.updateCouponsHistory(editDto, currentRealUser);
        return R.success("更新成功", true);
    }

    /**
     * 优惠券历史记录删除接口
     *
     * @param id ID
     */
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable String id) {
        // 获取当前操作用户
        User currentRealUser = securityTools.getLoginUser(StpUtil.getLoginIdAsString());
        // 调用service删除
        couponsHistoryService.deleteCouponsHistory(id, currentRealUser);
        return R.success("删除成功", true);
    }

    /**
     * 优惠券历史记录批量删除接口
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
        couponsHistoryService.deleteCouponsHistoryBatch(idList, currentRealUser);

        return R.success("批量删除成功", true);
    }

    /**
     * 根据ID查询优惠券历史记录接口
     *
     * @param id ID
     */
    @GetMapping("/{id}")
    public R<CouponsHistoryEditDTO> queryCouponsHistoryById(@PathVariable String id) {
        CouponsHistoryEditDTO couponsHistoryEditDTO = couponsHistoryService.queryCouponsHistoryById(id);
        return R.success("查询成功", couponsHistoryEditDTO);
    }

    /**
     * 优惠券历史记录数据导出接口
     *
     * @param predicate 条件谓语
     * @param response 相应
     * @throws IOException IO异常
     */
    @GetMapping("/export")
    public void export(@QuerydslPredicate(root = CouponsHistory.class) Predicate predicate,
        HttpServletResponse response) throws IOException {
        String fileName = "优惠券历史记录列表导出.xls";
        exportExcelBefore(response, fileName);
        EasyExcel.write(response.getOutputStream(), CouponsHistoryDTO.class)
            .sheet("Sheet1")
            .doWrite(couponsHistoryService.export(PredicateWrapper.of(predicate)));
    }

    /**
     * 优惠券历史记录表单数据初始化
     *
     * @return Map
     */
    //@GetMapping("/initCouponsHistoryFormData")
    //public R<Map<String, Object>> initCouponsHistoryFormData() {
    //    Map<String, Object> resultMap = new HashMap<>();
    //    // TODO: 数据填充
    //    return R.success(resultMap);
    //}

}
