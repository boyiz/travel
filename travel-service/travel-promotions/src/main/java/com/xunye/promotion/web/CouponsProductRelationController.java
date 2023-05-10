//package com.xunye.promotion.web;
//
//import cn.dev33.satoken.annotation.SaCheckRole;
//import cn.hutool.core.util.StrUtil;
//import com.alibaba.excel.EasyExcel;
//import com.xunye.core.result.R;
////import com.xunye.core.annotation.ApiAuth;
//import com.xunye.core.base.BaseController;
//import com.xunye.core.model.PredicateWrapper;
//import com.xunye.promotion.dto.CouponsProductRelationDTO;
//import com.xunye.promotion.dto.CouponsProductRelationEditDTO;
//import com.xunye.promotion.entity.CouponsProductRelation;
//import com.xunye.promotion.service.ICouponsProductRelationService;
//import com.querydsl.core.types.Predicate;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.querydsl.binding.QuerydslPredicate;
//import org.springframework.web.bind.annotation.*;
//
//import com.xunye.auth.entity.User;
//import com.xunye.core.tools.CheckTools;
//import com.xunye.auth.tool.SecurityTools;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import java.util.Map;
//import java.util.HashMap;
//
//@RestController
//@RequestMapping("/promotion/coupons-product-relation")
//@SaCheckRole("admin")
//public class CouponsProductRelationController extends BaseController {
//
//    @Resource
//    private ICouponsProductRelationService couponsProductRelationService;
//    @Resource
//    private SecurityTools securityTools;
//    /**
//     * 优惠券-商品 关系表列表查询接口
//     *
//     * @param predicate 条件谓语
//     * @param pageable  分页参数
//     */
//    @GetMapping
//    //@ApiAuth("promotion:coupons_product_relation:list")
//    public R<List<CouponsProductRelationDTO>> list(@QuerydslPredicate(root = CouponsProductRelation.class) Predicate predicate,
//                                         Pageable pageable) {
//        return couponsProductRelationService.queryCouponsProductRelationListByPage(PredicateWrapper.of(predicate), pageable);
//    }
//
//    /**
//     * 优惠券-商品 关系表创建接口
//     *
//     * @param addDTO 创建信息
//     */
//    @PostMapping
//    //@ApiAuth("promotion:coupons_product_relation:create")
//    public R<String> create(@RequestBody CouponsProductRelationEditDTO addDTO) {
//        User currentRealUser = securityTools.getLoginUser("");
//
//        // 创建并返回dbId
//        String dbId = couponsProductRelationService.createCouponsProductRelation(addDTO, currentRealUser);
//        return R.success("创建成功",dbId);
//    }
//
//    /**
//     * 优惠券-商品 关系表更新接口
//     *
//     * @param editDto 更新信息
//     */
//    @PutMapping
//    //@ApiAuth("promotion:coupons_product_relation:update")
//    public R<Boolean> update(@RequestBody CouponsProductRelationEditDTO editDto) {
//        // 基础校验
//        if (CheckTools.isNullOrEmpty(editDto.getId())) {
//            return R.failure("优惠券-商品 关系表id不存在" );
//        }
//
//        User currentRealUser = securityTools.getLoginUser("");
//
//        // 更新优惠券-商品 关系表
//        couponsProductRelationService.updateCouponsProductRelation(editDto, currentRealUser);
//        return R.success("更新成功", true);
//    }
//
//    /**
//     * 优惠券-商品 关系表删除接口
//     *
//     * @param id ID
//     */
//    @DeleteMapping("/{id}")
//    //@ApiAuth("promotion:coupons_product_relation:delete")
//    public R<Boolean> delete(@PathVariable String id) {
//        // 获取当前操作用户
//        User currentRealUser = securityTools.getLoginUser("");
//        // 调用service删除
//        couponsProductRelationService.deleteCouponsProductRelation(id,currentRealUser);
//        return R.success("删除成功", true);
//    }
//
//    /**
//     * 优惠券-商品 关系表批量删除接口
//     *
//     * @param ids id集合
//     */
//    @DeleteMapping("/batch/{ids}")
//    //@ApiAuth("promotion:coupons_product_relation:deleteBatch")
//    public R<Boolean> deleteBatch(@PathVariable String ids) {
//        // 参数预处理
//        if (StrUtil.isEmptyIfStr(ids)) return R.failure();
//
//        // 获取当前操作用户
//        User currentRealUser = securityTools.getLoginUser("");
//        // 调用service删除
//        List<String> idList = Arrays.stream(ids.split(",")).collect(Collectors.toList());
//        couponsProductRelationService.deleteCouponsProductRelationBatch(idList,currentRealUser);
//
//        return R.success("批量删除成功", true);
//    }
//
//    /**
//     * 根据ID查询优惠券-商品 关系表接口
//     *
//     * @param id ID
//     */
//    @GetMapping("/{id}")
//    //@ApiAuth("promotion:coupons_product_relation:queryById")
//    public R<CouponsProductRelationEditDTO> queryCouponsProductRelationById(@PathVariable String id) {
//        CouponsProductRelationEditDTO couponsProductRelationEditDTO = couponsProductRelationService.queryCouponsProductRelationById(id);
//        return R.success("查询成功",couponsProductRelationEditDTO);
//    }
//
//    /**
//     * 优惠券-商品 关系表数据导出接口
//     *
//     * @param predicate 条件谓语
//     * @param response  相应
//     * @throws IOException IO异常
//     */
//    @GetMapping("/export")
//    //@ApiAuth("promotion:coupons_product_relation:export")
//    public void export(@QuerydslPredicate(root = CouponsProductRelation.class) Predicate predicate, HttpServletResponse response) throws IOException {
//        String fileName = "优惠券-商品 关系表列表导出.xls";
//        exportExcelBefore(response, fileName);
//        EasyExcel.write(response.getOutputStream(), CouponsProductRelationDTO.class)
//                .sheet("Sheet1")
//                .doWrite(couponsProductRelationService.export(PredicateWrapper.of(predicate)));
//    }
//
//    /**
//     * 优惠券-商品 关系表表单数据初始化
//     *
//     * @return Map
//     */
//    @GetMapping("/initCouponsProductRelationFormData")
//    public R<Map<String, Object>> initCouponsProductRelationFormData() {
//        Map<String, Object> resultMap = new HashMap<>();
//        // TODO: 数据填充
//        return R.success(resultMap);
//    }
//
//}
