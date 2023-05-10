package com.xunye.order.web;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.excel.EasyExcel;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.xunye.auth.entity.User;
import com.xunye.auth.tool.SecurityTools;
import com.xunye.core.base.BaseController;
import com.xunye.core.model.PredicateWrapper;
import com.xunye.core.result.R;
import com.xunye.core.tools.CheckTools;
import com.xunye.core.tools.RedisUtil;
import com.xunye.order.dto.OrderInfoDTO;
import com.xunye.order.dto.OrderInfoEditDTO;
import com.xunye.order.entity.OrderInfo;
import com.xunye.order.entity.QOrderInfo;
import com.xunye.order.service.IOrderInfoService;
import com.xunye.order.cmd.OrderSubmitCmd;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.xunye.common.constant.OrderConstant.USER_ORDER_TOKEN_PREFIX;

@RestController
@RequestMapping("/order")
@SaCheckLogin
public class OrderInfoController extends BaseController {

    @Resource
    private IOrderInfoService orderInfoService;
    @Resource
    private SecurityTools securityTools;
    @Resource
    private RedisUtil redisUtil;
    /**
     * 订单表列表查询接口
     *
     * @param predicate 条件谓语
     * @param pageable  分页参数
     */
    @GetMapping("/all")
    @SaCheckRole("admin")
    public R<List<OrderInfoDTO>> allList(@QuerydslPredicate(root = OrderInfo.class) Predicate predicate,
                                         Pageable pageable) {
        return orderInfoService.queryOrderInfoListByPage(PredicateWrapper.of(predicate), pageable);
    }

    @GetMapping
    public R<List<OrderInfoDTO>> list(@QuerydslPredicate(root = OrderInfo.class) Predicate predicate,
        Pageable pageable) {
        QOrderInfo qOrderInfo = QOrderInfo.orderInfo;
        predicate = ExpressionUtils.and(predicate, qOrderInfo.userId.eq(StpUtil.getLoginIdAsString()));
        return orderInfoService.queryOrderInfoListByPage(PredicateWrapper.of(predicate), pageable);
    }

    @GetMapping("/token")
    public R<String> getOrderToken(){
        // 获取当前登录用户信息
        String loginIdAsString = StpUtil.getLoginIdAsString();
        //User currentRealUser = securityTools.getLoginUser(loginIdAsString);
        //生成防重令牌，有效期20分钟
        String token = UUID.randomUUID().toString().replace("-", "");
        redisUtil.set(USER_ORDER_TOKEN_PREFIX + loginIdAsString, token, 20 * 60);
        return R.success("创建成功",token);
    }

    //订单提交
    @PostMapping("/submit")
    public R<String> submitOrder(@RequestBody OrderSubmitCmd orderSubmitCmd) throws Exception{
        // 提交数据完整性检查
        orderSubmitCmd.check();
        // 获取当前登录用户信息
        String loginIdAsString = StpUtil.getLoginIdAsString();
        User currentRealUser = securityTools.getLoginUser(loginIdAsString);
        // 验证orderToken
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        long checkResult = redisUtil.execute(new DefaultRedisScript<Long>(script, Long.class),
            Arrays.asList(USER_ORDER_TOKEN_PREFIX + loginIdAsString, orderSubmitCmd.getOrderToken()));
        if (checkResult == 0L) {
            //令牌验证失败
            return R.success("订单提交超时，请重试");
        } else {
            // 创建并返回dbId
            String dbId = orderInfoService.createOrderInfo(orderSubmitCmd, currentRealUser);
            return R.success("创建成功", dbId);
        }
    }

    /**
     * 订单表创建接口
     *
     * @param addDTO 创建信息
     */
    //@PostMapping
    //public R<String> create(@RequestBody OrderInfoEditDTO addDTO) {
    //    User currentRealUser = securityTools.getLoginUser("");
    //
    //    // 创建并返回dbId
    //    String dbId = orderInfoService.createOrderInfo(addDTO, currentRealUser);
    //    return R.success("创建成功",dbId);
    //}

    /**
     * 订单表更新接口
     *
     * @param editDto 更新信息
     */
    @PutMapping
    @SaCheckRole("admin")
    public R<Boolean> update(@RequestBody OrderInfoEditDTO editDto) {
        // 基础校验
        if (CheckTools.isNullOrEmpty(editDto.getId())) {
            return R.failure("订单表id不存在" );
        }

        User currentRealUser = securityTools.getLoginUser(StpUtil.getLoginIdAsString());

        // 更新订单表
        orderInfoService.updateOrderInfo(editDto, currentRealUser);
        return R.success("更新成功", true);
    }

    /**
     * 订单表删除接口
     *
     * @param id ID
     */
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable String id) {
        // 获取当前操作用户
        User currentRealUser = securityTools.getLoginUser(StpUtil.getLoginIdAsString());
        // 调用service删除
        orderInfoService.deleteOrderInfo(id,currentRealUser);
        return R.success("删除成功", true);
    }

    /**
     * 订单表批量删除接口
     *
     * @param ids id集合
     */
    @DeleteMapping("/batch/{ids}")
    @SaCheckRole("admin")
    public R<Boolean> deleteBatch(@PathVariable String ids) {
        // 参数预处理
        if (StrUtil.isEmptyIfStr(ids)) return R.failure();

        // 获取当前操作用户
        User currentRealUser = securityTools.getLoginUser("");
        // 调用service删除
        List<String> idList = Arrays.stream(ids.split(",")).collect(Collectors.toList());
        orderInfoService.deleteOrderInfoBatch(idList,currentRealUser);

        return R.success("批量删除成功", true);
    }

    /**
     * 根据ID查询订单表接口
     *
     * @param id ID
     */
    @GetMapping("/{id}")
    @SaCheckRole("admin")
    public R<OrderInfoEditDTO> queryOrderInfoById(@PathVariable String id) {
        OrderInfoEditDTO orderInfoEditDTO = orderInfoService.queryOrderInfoById(id);
        return R.success("查询成功",orderInfoEditDTO);
    }

    /**
     * 订单表数据导出接口
     *
     * @param predicate 条件谓语
     * @param response  相应
     * @throws IOException IO异常
     */
    @GetMapping("/export")
    @SaCheckRole("admin")
    public void export(@QuerydslPredicate(root = OrderInfo.class) Predicate predicate, HttpServletResponse response) throws IOException {
        String fileName = "订单表列表导出.xls";
        exportExcelBefore(response, fileName);
        EasyExcel.write(response.getOutputStream(), OrderInfoDTO.class)
                .sheet("Sheet1")
                .doWrite(orderInfoService.export(PredicateWrapper.of(predicate)));
    }


}
