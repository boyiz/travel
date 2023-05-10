package com.xunye.promotion.web;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.excel.EasyExcel;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.hutool.core.util.StrUtil;
import com.querydsl.core.types.Predicate;
import com.xunye.auth.entity.User;
import com.xunye.auth.tool.SecurityTools;
import com.xunye.core.base.BaseController;
import com.xunye.core.model.PredicateWrapper;
import com.xunye.core.result.R;
import com.xunye.core.tools.CheckTools;
import com.xunye.promotion.dto.PolicyInfoDTO;
import com.xunye.promotion.dto.PolicyInfoEditDTO;
import com.xunye.promotion.entity.PolicyInfo;
import com.xunye.promotion.service.IPolicyInfoService;
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
@RequestMapping("/promotion/policy")
@SaCheckRole("admin")
public class PolicyInfoController extends BaseController {

    @Resource
    private IPolicyInfoService policyInfoService;
    @Resource
    private SecurityTools securityTools;

    /**
     * 优惠政策实体列表查询接口
     *
     * @param predicate 条件谓语
     * @param pageable  分页参数
     */
    @GetMapping
    public R<List<PolicyInfoDTO>> list(@QuerydslPredicate(root = PolicyInfo.class) Predicate predicate,
                                         Pageable pageable) {
        return policyInfoService.queryPolicyInfoListByPage(PredicateWrapper.of(predicate), pageable);
    }

    /**
     * 优惠政策实体创建接口
     *
     * @param addDTO 创建信息
     */
    @PostMapping
    public R<String> create(@RequestBody PolicyInfoEditDTO addDTO) {
        User currentRealUser = securityTools.getLoginUser("");

        // 创建并返回dbId
        String dbId = policyInfoService.createPolicyInfo(addDTO, currentRealUser);
        return R.success("创建成功",dbId);
    }

    /**
     * 优惠政策实体更新接口
     *
     * @param editDto 更新信息
     */
    @PutMapping
    public R<Boolean> update(@RequestBody PolicyInfoEditDTO editDto) {
        // 基础校验
        if (CheckTools.isNullOrEmpty(editDto.getId())) {
            return R.failure("优惠政策实体id不存在" );
        }

        User currentRealUser = securityTools.getLoginUser("");

        // 更新优惠政策实体
        policyInfoService.updatePolicyInfo(editDto, currentRealUser);
        return R.success("更新成功", true);
    }

    /**
     * 优惠政策实体删除接口
     *
     * @param id ID
     */
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable String id) {
        // 获取当前操作用户
        User currentRealUser = securityTools.getLoginUser("");
        // 调用service删除
        policyInfoService.deletePolicyInfo(id,currentRealUser);
        return R.success("删除成功", true);
    }

    /**
     * 优惠政策实体批量删除接口
     *
     * @param ids id集合
     */
    @DeleteMapping("/batch/{ids}")
    public R<Boolean> deleteBatch(@PathVariable String ids) {
        // 参数预处理
        if (StrUtil.isEmptyIfStr(ids)) return R.failure();

        // 获取当前操作用户
        User currentRealUser = securityTools.getLoginUser("");
        // 调用service删除
        List<String> idList = Arrays.stream(ids.split(",")).collect(Collectors.toList());
        policyInfoService.deletePolicyInfoBatch(idList,currentRealUser);

        return R.success("批量删除成功", true);
    }

    /**
     * 根据ID查询优惠政策实体接口
     *
     * @param id ID
     */
    @GetMapping("/{id}")
    public R<PolicyInfoEditDTO> queryPolicyInfoById(@PathVariable String id) {
        PolicyInfoEditDTO policyInfoEditDTO = policyInfoService.queryPolicyInfoById(id);
        return R.success("查询成功",policyInfoEditDTO);
    }

    /**
     * 优惠政策实体数据导出接口
     *
     * @param predicate 条件谓语
     * @param response  相应
     * @throws IOException IO异常
     */
    @GetMapping("/export")
    public void export(@QuerydslPredicate(root = PolicyInfo.class) Predicate predicate, HttpServletResponse response) throws IOException {
        String fileName = "优惠政策实体列表导出.xls";
        exportExcelBefore(response, fileName);
        EasyExcel.write(response.getOutputStream(), PolicyInfoDTO.class)
                .sheet("Sheet1")
                .doWrite(policyInfoService.export(PredicateWrapper.of(predicate)));
    }

    /**
     * 优惠政策实体表单数据初始化
     *
     * @return Map
     */
    @GetMapping("/initPolicyInfoFormData")
    public R<Map<String, Object>> initPolicyInfoFormData() {
        Map<String, Object> resultMap = new HashMap<>();
        // TODO: 数据填充
        return R.success(resultMap);
    }

}
