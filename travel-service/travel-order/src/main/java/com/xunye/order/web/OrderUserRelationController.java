package com.xunye.order.web;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.xunye.core.result.R;
import com.xunye.core.base.BaseController;
import com.xunye.core.model.PredicateWrapper;
import com.xunye.order.dto.OrderUserRelationDTO;
import com.xunye.order.dto.OrderUserRelationEditDTO;
import com.xunye.order.entity.OrderUserRelation;
import com.xunye.order.service.IOrderUserRelationService;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.web.bind.annotation.*;

import com.xunye.auth.entity.User;
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
@RequestMapping("/order/user-relation")
@SaCheckRole("admin")
public class OrderUserRelationController extends BaseController {

    @Resource
    private IOrderUserRelationService orderUserRelationService;
    @Resource
    private SecurityTools securityTools;
    /**
     * 用户订单关联用户列表查询接口
     *
     * @param predicate 条件谓语
     * @param pageable  分页参数
     */
    @GetMapping
    public R<List<OrderUserRelationDTO>> list(@QuerydslPredicate(root = OrderUserRelation.class) Predicate predicate,
                                         Pageable pageable) {
        return orderUserRelationService.queryOrderUserRelationListByPage(PredicateWrapper.of(predicate), pageable);
    }

    /**
     * 用户订单关联用户创建接口
     *
     * @param addDTO 创建信息
     */
    @PostMapping
    public R<String> create(@RequestBody OrderUserRelationEditDTO addDTO) {
        User currentRealUser = securityTools.getLoginUser("");

        // 创建并返回dbId
        String dbId = orderUserRelationService.createOrderUserRelation(addDTO, currentRealUser);
        return R.success("创建成功",dbId);
    }

    /**
     * 用户订单关联用户更新接口
     *
     * @param editDto 更新信息
     */
    @PutMapping
    public R<Boolean> update(@RequestBody OrderUserRelationEditDTO editDto) {
        // 基础校验
        //if (CheckTools.isNullOrEmpty(editDto.getId())) {
        //    return R.failure("用户订单关联用户id不存在" );
        //}
        //
        //User currentRealUser = securityTools.getLoginUser("");
        //
        //// 更新用户订单关联用户
        //orderUserRelationService.updateOrderUserRelation(editDto, currentRealUser);
        return R.success("更新成功", true);
    }

    /**
     * 用户订单关联用户删除接口
     *
     * @param id ID
     */
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable String id) {
        // 获取当前操作用户
        User currentRealUser = securityTools.getLoginUser("");
        // 调用service删除
        orderUserRelationService.deleteOrderUserRelation(id,currentRealUser);
        return R.success("删除成功", true);
    }

    /**
     * 用户订单关联用户批量删除接口
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
        orderUserRelationService.deleteOrderUserRelationBatch(idList,currentRealUser);

        return R.success("批量删除成功", true);
    }

    /**
     * 根据ID查询用户订单关联用户接口
     *
     * @param id ID
     */
    @GetMapping("/{id}")
    public R<OrderUserRelationEditDTO> queryOrderUserRelationById(@PathVariable String id) {
        OrderUserRelationEditDTO orderUserRelationEditDTO = orderUserRelationService.queryOrderUserRelationById(id);
        return R.success("查询成功",orderUserRelationEditDTO);
    }

    /**
     * 用户订单关联用户数据导出接口
     *
     * @param predicate 条件谓语
     * @param response  相应
     * @throws IOException IO异常
     */
    @GetMapping("/export")
    public void export(@QuerydslPredicate(root = OrderUserRelation.class) Predicate predicate, HttpServletResponse response) throws IOException {
        String fileName = "用户订单关联用户列表导出.xls";
        exportExcelBefore(response, fileName);
        EasyExcel.write(response.getOutputStream(), OrderUserRelationDTO.class)
                .sheet("Sheet1")
                .doWrite(orderUserRelationService.export(PredicateWrapper.of(predicate)));
    }

    /**
     * 用户订单关联用户表单数据初始化
     *
     * @return Map
     */
    //@GetMapping("/initOrderUserRelationFormData")
    //public R<Map<String, Object>> initOrderUserRelationFormData() {
    //    Map<String, Object> resultMap = new HashMap<>();
    //    // TODO: 数据填充
    //    return R.success(resultMap);
    //}

}
