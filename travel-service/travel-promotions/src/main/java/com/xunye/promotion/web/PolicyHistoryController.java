package com.xunye.promotion.web;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.xunye.core.result.R;
//import com.xunye.core.annotation.ApiAuth;
import com.xunye.core.base.BaseController;
import com.xunye.core.model.PredicateWrapper;
import com.xunye.promotion.dto.PolicyHistoryDTO;
import com.xunye.promotion.dto.PolicyHistoryEditDTO;
import com.xunye.promotion.entity.PolicyHistory;
import com.xunye.promotion.service.IPolicyHistoryService;
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
@RequestMapping("/promotion/policy-history")
@SaCheckRole("admin")
public class PolicyHistoryController extends BaseController {

    @Resource
    private IPolicyHistoryService policyHistoryService;
    @Resource
    private SecurityTools securityTools;
    /**
     * 优惠政策使用记录列表查询接口
     *
     * @param predicate 条件谓语
     * @param pageable  分页参数
     */
    @GetMapping
    //@ApiAuth("promotion:policy-history:list")
    public R<List<PolicyHistoryDTO>> list(@QuerydslPredicate(root = PolicyHistory.class) Predicate predicate,
                                         Pageable pageable) {
        return policyHistoryService.queryPolicyHistoryListByPage(PredicateWrapper.of(predicate), pageable);
    }

    /**
     * 优惠政策使用记录创建接口
     *
     * @param addDTO 创建信息
     */
    @PostMapping
    //@ApiAuth("promotion:policy-history:create")
    public R<String> create(@RequestBody PolicyHistoryEditDTO addDTO) {
        User currentRealUser = securityTools.getLoginUser("");

        // 创建并返回dbId
        String dbId = policyHistoryService.createPolicyHistory(addDTO, currentRealUser);
        return R.success("创建成功",dbId);
    }

    /**
     * 优惠政策使用记录更新接口
     *
     * @param editDto 更新信息
     */
    @PutMapping
    //@ApiAuth("promotion:policy-history:update")
    public R<Boolean> update(@RequestBody PolicyHistoryEditDTO editDto) {
        // 基础校验
        if (CheckTools.isNullOrEmpty(editDto.getId())) {
            return R.failure("优惠政策使用记录id不存在" );
        }

        User currentRealUser = securityTools.getLoginUser("");

        // 更新优惠政策使用记录
        policyHistoryService.updatePolicyHistory(editDto, currentRealUser);
        return R.success("更新成功", true);
    }

    /**
     * 优惠政策使用记录删除接口
     *
     * @param id ID
     */
    @DeleteMapping("/{id}")
    //@ApiAuth("promotion:policy-history:delete")
    public R<Boolean> delete(@PathVariable String id) {
        // 获取当前操作用户
        User currentRealUser = securityTools.getLoginUser("");
        // 调用service删除
        policyHistoryService.deletePolicyHistory(id,currentRealUser);
        return R.success("删除成功", true);
    }

    /**
     * 优惠政策使用记录批量删除接口
     *
     * @param ids id集合
     */
    @DeleteMapping("/batch/{ids}")
    //@ApiAuth("promotion:policy-history:deleteBatch")
    public R<Boolean> deleteBatch(@PathVariable String ids) {
        // 参数预处理
        if (StrUtil.isEmptyIfStr(ids)) return R.failure();

        // 获取当前操作用户
        User currentRealUser = securityTools.getLoginUser("");
        // 调用service删除
        List<String> idList = Arrays.stream(ids.split(",")).collect(Collectors.toList());
        policyHistoryService.deletePolicyHistoryBatch(idList,currentRealUser);

        return R.success("批量删除成功", true);
    }

    /**
     * 根据ID查询优惠政策使用记录接口
     *
     * @param id ID
     */
    @GetMapping("/{id}")
    //@ApiAuth("promotion:policy-history:queryById")
    public R<PolicyHistoryEditDTO> queryPolicyHistoryById(@PathVariable String id) {
        PolicyHistoryEditDTO policyHistoryEditDTO = policyHistoryService.queryPolicyHistoryById(id);
        return R.success("查询成功",policyHistoryEditDTO);
    }

    /**
     * 优惠政策使用记录数据导出接口
     *
     * @param predicate 条件谓语
     * @param response  相应
     * @throws IOException IO异常
     */
    @GetMapping("/export")
    //@ApiAuth("promotion:policy-history:export")
    public void export(@QuerydslPredicate(root = PolicyHistory.class) Predicate predicate, HttpServletResponse response) throws IOException {
        String fileName = "优惠政策使用记录列表导出.xls";
        exportExcelBefore(response, fileName);
        EasyExcel.write(response.getOutputStream(), PolicyHistoryDTO.class)
                .sheet("Sheet1")
                .doWrite(policyHistoryService.export(PredicateWrapper.of(predicate)));
    }

    /**
     * 优惠政策使用记录表单数据初始化
     *
     * @return Map
     */
    @GetMapping("/initPolicyHistoryFormData")
    public R<Map<String, Object>> initPolicyHistoryFormData() {
        Map<String, Object> resultMap = new HashMap<>();
        // TODO: 数据填充
        return R.success(resultMap);
    }

}
