package com.xunye.order.web;

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
import com.xunye.order.dto.OrderItemDTO;
import com.xunye.order.dto.OrderItemEditDTO;
import com.xunye.order.entity.OrderItem;
import com.xunye.order.service.IOrderItemService;
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
@RequestMapping("/order/item")
@SaCheckRole("admin")
public class OrderItemController extends BaseController {

    @Resource
    private IOrderItemService orderItemService;
    @Resource
    private SecurityTools securityTools;
    /**
     * 订单详情列表查询接口
     *
     * @param predicate 条件谓语
     * @param pageable  分页参数
     */
    @GetMapping
    public R<List<OrderItemDTO>> list(@QuerydslPredicate(root = OrderItem.class) Predicate predicate,
                                         Pageable pageable) {
        return orderItemService.queryOrderItemListByPage(PredicateWrapper.of(predicate), pageable);
    }

    /**
     * 订单详情创建接口
     *
     * @param addDTO 创建信息
     */
    @PostMapping
    public R<String> create(@RequestBody OrderItemEditDTO addDTO) {
        User currentRealUser = securityTools.getLoginUser("");

        // 创建并返回dbId
        String dbId = orderItemService.createOrderItem(addDTO, currentRealUser);
        return R.success("创建成功",dbId);
    }

    /**
     * 订单详情更新接口
     *
     * @param editDto 更新信息
     */
    @PutMapping
    public R<Boolean> update(@RequestBody OrderItemEditDTO editDto) {
        // 基础校验
        if (CheckTools.isNullOrEmpty(editDto.getId())) {
            return R.failure("订单详情id不存在" );
        }

        User currentRealUser = securityTools.getLoginUser("");

        // 更新订单详情
        orderItemService.updateOrderItem(editDto, currentRealUser);
        return R.success("更新成功", true);
    }

    /**
     * 订单详情删除接口
     *
     * @param id ID
     */
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable String id) {
        // 获取当前操作用户
        User currentRealUser = securityTools.getLoginUser("");
        // 调用service删除
        orderItemService.deleteOrderItem(id,currentRealUser);
        return R.success("删除成功", true);
    }

    /**
     * 订单详情批量删除接口
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
        orderItemService.deleteOrderItemBatch(idList,currentRealUser);

        return R.success("批量删除成功", true);
    }

    /**
     * 根据ID查询订单详情接口
     *
     * @param id ID
     */
    @GetMapping("/{id}")
    public R<OrderItemEditDTO> queryOrderItemById(@PathVariable String id) {
        OrderItemEditDTO orderItemEditDTO = orderItemService.queryOrderItemById(id);
        return R.success("查询成功",orderItemEditDTO);
    }

    /**
     * 订单详情数据导出接口
     *
     * @param predicate 条件谓语
     * @param response  相应
     * @throws IOException IO异常
     */
    @GetMapping("/export")
    public void export(@QuerydslPredicate(root = OrderItem.class) Predicate predicate, HttpServletResponse response) throws IOException {
        String fileName = "订单详情列表导出.xls";
        exportExcelBefore(response, fileName);
        EasyExcel.write(response.getOutputStream(), OrderItemDTO.class)
                .sheet("Sheet1")
                .doWrite(orderItemService.export(PredicateWrapper.of(predicate)));
    }

    /**
     * 订单详情表单数据初始化
     *
     * @return Map
     */
    @GetMapping("/initOrderItemFormData")
    public R<Map<String, Object>> initOrderItemFormData() {
        Map<String, Object> resultMap = new HashMap<>();
        // TODO: 数据填充
        return R.success(resultMap);
    }

}
