package com.xunye.order.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.querydsl.core.types.ExpressionUtils;
import com.xunye.common.constant.GroupConstant;
import com.xunye.core.em.StatusEnum;
import com.xunye.core.result.R;
import com.xunye.core.base.BaseMapper;
import com.xunye.core.base.BaseRepository;
import com.xunye.core.base.BaseServiceImpl;
import com.xunye.core.model.PredicateWrapper;
import com.xunye.core.tools.ComputeTools;
import com.xunye.core.tools.RedisUtil;
import com.xunye.group.dto.GroupInfoEditDTO;
import com.xunye.group.entity.QGroupInfo;
import com.xunye.group.service.IGroupInfoService;
import com.xunye.order.dto.OrderInfoDTO;
import com.xunye.order.dto.OrderInfoEditDTO;
import com.xunye.order.dto.OrderItemEditDTO;
import com.xunye.order.dto.OrderUserRelationEditDTO;
import com.xunye.order.em.OrderStatusEnum;
import com.xunye.order.entity.OrderInfo;
import com.xunye.order.entity.QOrderInfo;
import com.xunye.order.mapper.OrderInfoMapper;
import com.xunye.order.notify.CouponsHistoryNotifier;
import com.xunye.order.notify.GroupNotifier;
import com.xunye.order.notify.PolicyHistoryNotifier;
import com.xunye.order.repo.OrderInfoRepository;
import com.xunye.order.service.IOrderInfoService;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Predicate;
import com.xunye.order.cmd.OrderSubmitCmd;
import com.xunye.order.service.IOrderItemService;
import com.xunye.order.service.IOrderUserRelationService;
import com.xunye.order.vo.TripPerson;
import com.xunye.promotion.dto.CouponsEditDTO;
import com.xunye.promotion.dto.CouponsHistoryEditDTO;
import com.xunye.promotion.dto.PolicyInfoEditDTO;
import com.xunye.promotion.em.CouponsUseStatusEnum;
import com.xunye.promotion.entity.CouponsHistory;
import com.xunye.promotion.entity.QCoupons;
import com.xunye.promotion.entity.QCouponsHistory;
import com.xunye.promotion.entity.QPolicyInfo;
import com.xunye.promotion.service.ICouponsHistoryService;
import com.xunye.promotion.service.ICouponsService;
import com.xunye.promotion.service.IPolicyInfoService;
import com.xunye.route.service.IRouteInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.concurrent.ThreadPoolExecutor;

import java.util.Optional;

import com.xunye.core.tools.CheckTools;
import com.xunye.auth.entity.User;
import com.xunye.core.exception.BusinessException;

@Slf4j
@Service
public class OrderInfoServiceImpl extends BaseServiceImpl<OrderInfoEditDTO, OrderInfoDTO, OrderInfo, String>
    implements IOrderInfoService {

    private ThreadLocal<OrderSubmitCmd> confirmOrderThreadLocal = new ThreadLocal<>();

    private static final QOrderInfo qOrderInfo = QOrderInfo.orderInfo;

    @Autowired
    private PolicyHistoryNotifier policyHistoryNotifier;
    @Autowired
    private CouponsHistoryNotifier couponHistoryNotifier;
    @Autowired
    private GroupNotifier groupNotifier;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ThreadPoolExecutor executor;

    private final OrderInfoMapper orderInfoMapper;
    private final OrderInfoRepository orderInfoRepository;
    private final IOrderItemService orderItemService;
    private final IPolicyInfoService policyInfoService;
    private final ICouponsHistoryService couponsHistoryService;
    private final IRouteInfoService routeInfoService;
    private final IGroupInfoService groupInfoService;
    private final ICouponsService couponService;
    private final IOrderUserRelationService orderUserRelationService;

    public OrderInfoServiceImpl(OrderInfoMapper orderInfoMapper, OrderInfoRepository orderInfoRepository,
        IOrderItemService orderItemService, IPolicyInfoService policyInfoService,
        ICouponsHistoryService couponsHistoryService, IRouteInfoService routeInfoService,
        IGroupInfoService groupInfoService,
        ICouponsService couponService, IOrderUserRelationService orderUserRelationService) {
        this.orderInfoMapper = orderInfoMapper;
        this.orderInfoRepository = orderInfoRepository;
        this.orderItemService = orderItemService;
        this.policyInfoService = policyInfoService;
        this.couponsHistoryService = couponsHistoryService;
        this.routeInfoService = routeInfoService;
        this.groupInfoService = groupInfoService;
        this.couponService = couponService;
        this.orderUserRelationService = orderUserRelationService;
    }

    /**
     * 创建订单表信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createOrderInfo(OrderSubmitCmd orderSubmitCmd, User operatorUser)
        throws ExecutionException, InterruptedException {
        //TODO 理论上应该写在Biz层中，不破坏原有Service层，后期修改
        confirmOrderThreadLocal.set(orderSubmitCmd);
        QGroupInfo qGroupInfo = QGroupInfo.groupInfo;
        QPolicyInfo qPolicyInfo = QPolicyInfo.policyInfo;
        QCoupons qCoupons = QCoupons.coupons;
        QCouponsHistory qCouponsHistory = QCouponsHistory.couponsHistory;

        //解析OrderSubmitCmd实体
        //赋值OrderInfo
        OrderInfo orderInfoEntity = new OrderInfo();
        orderInfoEntity.setUserId(operatorUser.getId());
        //用于订单Sn
        String timeToSnId = IdWorker.getTimeId();
        orderInfoEntity.setOrderSn(timeToSnId);
        /*
            查询数据库，获取路线ID和开团ID信息
            1、库存判断，库存减，用Redis
            2、设置totalAmount
        */
        //GROUP_STOCK:RouteID:GroupID
        String qryStockKey = GroupConstant.ROUTE_GROUP_STOCK_PREFIX + orderSubmitCmd.getRouteId() + ":" + orderSubmitCmd.getGroupId();
        int group_stock = Integer.parseInt(redisUtil.get(qryStockKey).toString());
        if (group_stock <= 0 || group_stock < orderSubmitCmd.getQuantity()) {
            throw new BusinessException("订单创建失败：当前团剩余人数不足，请选择其他团下单");
        }
        redisUtil.decrByUntilTo0(qryStockKey, orderSubmitCmd.getQuantity());

        AtomicReference<Double> totalAmount = new AtomicReference<>(0.0);
        AtomicReference<Double> policyAmount = new AtomicReference<>(0.0);
        AtomicReference<Double> couponAmount = new AtomicReference<>(0.0);

        CompletableFuture<Void> orderTotalAmountFuture = CompletableFuture.runAsync(() -> {
            Predicate qGroupInfo_Predicate = qGroupInfo.id.eq(orderSubmitCmd.getGroupId())
                .and(qGroupInfo.routeId.eq(orderSubmitCmd.getRouteId()));
            List<GroupInfoEditDTO> groupInfoEditDTOS = groupInfoService.queryGroupInfoEditDTOListByPredicate(
                qGroupInfo_Predicate);
            if (groupInfoEditDTOS.size() > 0) {
                double priceValue = Double.parseDouble(groupInfoEditDTOS.get(0).getPrice());
                if ((doubleCompare(priceValue, 0.0) == -1) || (doubleCompare(priceValue, 0.0) == 0)) {
                    throw new BusinessException("订单创建失败：订单价格异常，请重新提交订单");
                }
                totalAmount.set(ComputeTools.mul(priceValue, orderSubmitCmd.getQuantity()));
            }
            orderInfoEntity.setTotalAmount(totalAmount + "");
        }, executor);

        CompletableFuture<List<PolicyInfoEditDTO>> orderPolicyAmountFuture = CompletableFuture.supplyAsync(() -> {
            List<PolicyInfoEditDTO> policyCollect = new ArrayList<>();
            //查询数据库，获取活动信息和金额，设置policyAmount
            // 如果未使用Policy优惠则直接跳过计算policyAmount
            if (CheckTools.isNotNullOrEmpty(orderSubmitCmd.getPolicyList())
                && orderSubmitCmd.getPolicyList().size() != 0) {
                //构建查询
                Predicate qPolicyInfo_Predicate = qPolicyInfo.groupId.eq(orderSubmitCmd.getGroupId())
                    .and(qPolicyInfo.routeId.eq(orderSubmitCmd.getRouteId()))
                    .and(qPolicyInfo.status.eq(StatusEnum.Enable.getValue()))
                    .and(qPolicyInfo.effectiveTime.goe(new Date()))
                    .and(qPolicyInfo.failureTime.loe(new Date()));
                List<PolicyInfoEditDTO> policyInfoEditDTOS = policyInfoService.queryPolicyInfoEditDTOListByPredicate(
                    qPolicyInfo_Predicate);
                //校验：过滤两个list，取相同
                policyCollect = policyInfoEditDTOS.stream().filter(
                        result -> orderSubmitCmd.getPolicyList().stream().noneMatch(
                            list -> result.getId().equals(list)))
                    .collect(Collectors.toList());
                if (policyCollect.size() != 0) {
                    for (PolicyInfoEditDTO policyInfoEditDTO : policyCollect) {
                        policyAmount.set(
                            ComputeTools.add(policyAmount.get(), Double.parseDouble(policyInfoEditDTO.getPrice())));
                    }
                }
            }
            orderInfoEntity.setPolicyAmount(policyAmount + "");
            return policyCollect;
        }, executor);

        CompletableFuture<List<CouponsHistoryEditDTO>> orderCouponAmountFuture = CompletableFuture.supplyAsync(() -> {
        /*
            查询数据库，获取用户下的优惠券信息，和couponsList对比
            1、设置couponAmount
            2、修改CouponsHistory
         */

            List<CouponsHistoryEditDTO> couponsHistoryCollect = new ArrayList<>();
            //List<CouponsEditDTO> couponsCollect = new ArrayList<>();
            if (CheckTools.isNotNullOrEmpty(orderSubmitCmd.getCouponsList())
                && orderSubmitCmd.getCouponsList().size() != 0) {

                Predicate qCoupons_Predicate = qCouponsHistory.userId.eq(operatorUser.getId())
                    //.and(qCouponsHistory.id.eq(qCoupons.id))
                    .and(qCouponsHistory.useStatus.eq(CouponsUseStatusEnum.UNUSED.getValue()))
                    .and(qPolicyInfo.effectiveTime.goe(new Date()))
                    .and(qPolicyInfo.failureTime.loe(new Date()));
                QueryResults<CouponsHistory> queryCouponsResults = getJpaQueryFactory()
                    .select(qCouponsHistory)
                    .from(qCouponsHistory)
                    .where(qCoupons_Predicate)
                    .fetchResults();
                //QueryResults<Tuple> queryCouponsResults = getJpaQueryFactory()
                //    .select(qCoupons,qCouponsHistory)
                //    .from(qCoupons)
                //    .leftJoin(qCouponsHistory).on(qCouponsHistory.couponId.eq(qCoupons.id))
                //    .where(qCoupons_Predicate)
                //    .fetchResults();
                //List<CouponsHistoryEditDTO> couponsHistoryEditDTOList = new ArrayList<>();
                //for (Tuple tuple : queryCouponsResults.getResults()) {
                //    CouponsEditDTO couponsEditDTO = couponService.getMapper().toEditDTO(tuple.get(qCoupons));
                //    CouponsHistoryEditDTO couponsHistoryEditDTO = couponsHistoryService.getMapper().toEditDTO(tuple
                //    .get
                //    (qCouponsHistory));
                //    couponsEditDTOList.add(couponsEditDTO);
                //    couponsHistoryEditDTOList.add(couponsHistoryEditDTO);
                //}
            /*
                TODO 开始过滤，先搁置，优先级降低
                1、判断minPoint使用门槛
                2、判断useType使用类型
            */
                //couponsCollect = couponsEditDTOList
                //    //校验：过滤两个list，取相同
                //    .stream().filter(result -> orderSubmitCmd.getCouponsList().stream().noneMatch(
                //        list -> result.getId().equals(list)
                //    )).collect(Collectors.toList());
                couponsHistoryCollect = queryCouponsResults.getResults()
                    .stream().map(e -> couponsHistoryService.getMapper().toEditDTO(e))
                    .filter(
                        result -> orderSubmitCmd.getCouponsList().stream().noneMatch(
                            list -> result.getId().equals(list)
                        )).collect(Collectors.toList());

                for (CouponsHistoryEditDTO couponsHistoryEditDTO : couponsHistoryCollect) {
                    CouponsEditDTO couponsEditDTO = couponService.queryCouponsById(couponsHistoryEditDTO.getCouponId());
                    if (CheckTools.isNotNullOrEmpty(couponsEditDTO)) {
                        couponAmount.set(
                            ComputeTools.add(couponAmount.get(), Double.parseDouble(couponsEditDTO.getAmount())));
                    }
                }

            }
            orderInfoEntity.setCouponAmount(couponAmount + "");

            return couponsHistoryCollect;
        }, executor);

        CompletableFuture.allOf(orderTotalAmountFuture, orderPolicyAmountFuture, orderCouponAmountFuture).get();

        //计算获得payAmount
        //总计优惠金额 = 优惠券 + 活动
        double all_discount_amount = ComputeTools.add(couponAmount.get(), policyAmount.get());
        double payAmount = ComputeTools.sub(totalAmount.get(), all_discount_amount);
        orderInfoEntity.setPayAmount(payAmount + "");

        orderInfoEntity.setPayType(orderSubmitCmd.getPayType());
        //默认创建订单为未支付
        orderInfoEntity.setOrderStatus(OrderStatusEnum.WAIT_PAY.getValue());
        orderInfoEntity.setRemark(orderSubmitCmd.getRemark());

        // 设置创建信息
        if (CheckTools.isNotNullOrEmpty(operatorUser)) {
            orderInfoEntity.setCreateBy(operatorUser.getId());
            orderInfoEntity.setCreateByName(operatorUser.getUserName());
            orderInfoEntity.setUpdateBy(operatorUser.getId());
            orderInfoEntity.setUpdateByName(operatorUser.getUserName());
        } else {
            throw new BusinessException("订单创建失败：获取用户信息获取有误");
        }
        // 创建订单表
        orderInfoRepository.saveAndFlush(orderInfoEntity);

        //赋值OrderItem
        OrderItemEditDTO orderItemEntity = new OrderItemEditDTO();
        orderItemEntity.setOrderId(orderInfoEntity.getId());
        orderItemEntity.setOrderSn(timeToSnId);
        orderItemEntity.setRouteId(orderSubmitCmd.getRouteId());
        orderItemEntity.setGroupId(orderSubmitCmd.getGroupId());
        orderItemEntity.setQuantity(orderSubmitCmd.getQuantity().toString());
        orderItemEntity.setRoommate(orderSubmitCmd.getRoommate());
        orderItemEntity.setPolicyAmount(policyAmount + "");
        orderItemEntity.setCouponAmount(couponAmount + "");
        orderItemEntity.setRealAmount(payAmount + "");
        orderItemService.createOrderItem(orderItemEntity, operatorUser);

        // 出行人OrderUserRelation
        if (orderSubmitCmd.getQuantity() != orderSubmitCmd.getTripPersonList().size()) {
            throw new BusinessException("订单创建失败：出行人信息有误");
        }
        for (TripPerson tripPerson : orderSubmitCmd.getTripPersonList()) {
            //TODO 判断出行人信息是否有误，暂无必要先搁置
            OrderUserRelationEditDTO orderUserRelationEditDTO = new OrderUserRelationEditDTO();
            orderUserRelationEditDTO.setOrderId(orderInfoEntity.getId());
            orderUserRelationEditDTO.setOrderSn(orderInfoEntity.getOrderSn());
            orderUserRelationEditDTO.setRouteId(orderSubmitCmd.getRouteId());
            orderUserRelationEditDTO.setGroupId(orderSubmitCmd.getGroupId());
            orderUserRelationEditDTO.setUserId(tripPerson.getUserId());
            orderUserRelationEditDTO.setUserType(tripPerson.getUserType());
            orderUserRelationService.createOrderUserRelation(orderUserRelationEditDTO, operatorUser);
        }


        /* ----------------------------------事件监听---------------------------------------*/
        // 设置PolicyHistory事件监听
        // 将 policyCollect 赋值给变量，一同作为事件监听参数
        policyHistoryNotifier.createPolicyHistory(orderInfoEntity, orderPolicyAmountFuture.get(), operatorUser);

        // 设置CouponsHistory事件监听，修改CouponsHistory记录
        couponHistoryNotifier.updateCouponsHistory(orderInfoEntity, orderCouponAmountFuture.get(), operatorUser);

        // 修改团库存
        groupNotifier.updateGroupStock(orderSubmitCmd.getGroupId(), orderSubmitCmd.getQuantity());



        /* ----------------------------------消息队列---------------------------------------*/

        // TODO 发送订单ID到mq消息队列

        // 返回订单ID
        return orderInfoEntity.getId();
    }

    /**
     * 更新订单表信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrderInfo(OrderInfoEditDTO orderInfoEditDto, User operatorUser) {
        Optional<OrderInfo> optional = orderInfoRepository.findById(orderInfoEditDto.getId());
        if (!optional.isPresent()) {
            throw new BusinessException("订单表不存在");
        }

        // 将EditDto属性合并到DB实体中
        OrderInfo orderInfoDB = optional.get();
        orderInfoMapper.merge(orderInfoEditDto, orderInfoDB);
        // 设置更新信息
        orderInfoDB.setUpdateBy(operatorUser.getId());
        orderInfoDB.setUpdateByName(operatorUser.getUserName());
        // 更新订单表
        orderInfoRepository.saveAndFlush(orderInfoDB);
    }

    /**
     * 删除订单表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteOrderInfo(String id, User operatorUser) {
        // before
        Predicate predicate = qOrderInfo.id.eq(id);
        predicate = ExpressionUtils.and(predicate, qOrderInfo.userId.eq(operatorUser.getId()));
        List<OrderInfoEditDTO> orderInfoEditDTOS = queryOrderInfoEditDTOListByPredicate(predicate);
        if (CheckTools.isNotNullOrEmpty(orderInfoEditDTOS) && orderInfoEditDTOS.size() == 1) {
            deleteById(id);
        }
    }

    /**
     * 批量删除订单表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteOrderInfoBatch(List<String> ids, User operatorUser) {
        // 遍历删除
        for (String id : ids) {
            this.deleteOrderInfo(id, operatorUser);
        }
    }

    /**
     * 分页查询订单表列表
     */
    @Override
    public R<List<OrderInfoDTO>> queryOrderInfoListByPage(PredicateWrapper predicateWrapper, Pageable pageable) {
        Predicate predicate = predicateWrapper.getPredicate();

        QueryResults<OrderInfo> queryResults = getJpaQueryFactory()
            .select(qOrderInfo)
            .from(qOrderInfo)
            .where(predicate)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(qOrderInfo.createTime.desc())
            .fetchResults();

        List<OrderInfoDTO> dtoList = queryResults.getResults()
            .stream().map(entity -> getMapper().toBasicDTO(entity))
            .collect(Collectors.toList());
        return R.success(dtoList).setTotal(queryResults.getTotal());
    }

    /**
     * 根据Id查询订单表信息
     */
    @Override
    public OrderInfoEditDTO queryOrderInfoById(String id) {
        Predicate predicate = qOrderInfo.id.eq(id);
        List<OrderInfoEditDTO> queryList = queryOrderInfoEditDTOListByPredicate(predicate);
        if (queryList.size() == 1) {
            return queryList.get(0);
        }
        return null;
    }

    /**
     * 导出订单表数据
     */
    @Override
    public List<OrderInfoDTO> export(PredicateWrapper predicateWrapper) {
        long count = orderInfoRepository.count();
        R<List<OrderInfoDTO>> exportResult = this.queryOrderInfoListByPage(predicateWrapper,
            PageRequest.of(0, (int)count));
        return exportResult.getData();
    }

    @Override
    public List<OrderInfoEditDTO> queryOrderInfoEditDTOListByPredicate(Predicate predicate) {
        return getJpaQueryFactory()
            .selectFrom(qOrderInfo)
            .where(predicate)
            .orderBy(qOrderInfo.createTime.asc())
            .fetch()
            .stream()
            .map(entity -> getMapper().toEditDTO(entity))
            .collect(Collectors.toList());
    }

    @Override
    public OrderInfoEditDTO queryOrderInfoByOrderSn(String orderSn) {
        Predicate predicate = qOrderInfo.orderSn.eq(orderSn);
        List<OrderInfoEditDTO> queryList = queryOrderInfoEditDTOListByPredicate(predicate);
        if (queryList.size() == 1) {
            return queryList.get(0);
        }
        return null;
    }

    @Override
    public BaseRepository<OrderInfo, String> getRepository() {
        return orderInfoRepository;
    }

    @Override
    public BaseMapper<OrderInfo, OrderInfoDTO, OrderInfoEditDTO> getMapper() {
        return orderInfoMapper;
    }

    private int doubleCompare(double d1, double d2) {
        BigDecimal bda = new BigDecimal(d1);
        BigDecimal bdb = new BigDecimal(d2);
        return bda.compareTo(bdb);
    }
}
