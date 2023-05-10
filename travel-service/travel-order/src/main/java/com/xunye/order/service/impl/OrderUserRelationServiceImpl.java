package com.xunye.order.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Predicate;
import com.xunye.auth.entity.User;
import com.xunye.core.base.BaseMapper;
import com.xunye.core.base.BaseRepository;
import com.xunye.core.base.BaseServiceImpl;
import com.xunye.core.exception.BusinessException;
import com.xunye.core.model.PredicateWrapper;
import com.xunye.core.result.R;
import com.xunye.core.tools.CheckTools;
import com.xunye.order.dto.OrderUserRelationDTO;
import com.xunye.order.dto.OrderUserRelationEditDTO;
import com.xunye.order.entity.OrderUserRelation;
import com.xunye.order.entity.QOrderUserRelation;
import com.xunye.order.mapper.OrderUserRelationMapper;
import com.xunye.order.repo.OrderUserRelationRepository;
import com.xunye.order.service.IOrderUserRelationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
public class OrderUserRelationServiceImpl extends BaseServiceImpl<OrderUserRelationEditDTO, OrderUserRelationDTO, OrderUserRelation, String> implements IOrderUserRelationService {

    private static final QOrderUserRelation qOrderUserRelation = QOrderUserRelation.orderUserRelation;

    private final OrderUserRelationMapper orderUserRelationMapper;
    private final OrderUserRelationRepository orderUserRelationRepository;

    public OrderUserRelationServiceImpl(OrderUserRelationMapper orderUserRelationMapper, OrderUserRelationRepository orderUserRelationRepository) {
        this.orderUserRelationMapper = orderUserRelationMapper;
        this.orderUserRelationRepository = orderUserRelationRepository;
    }

    /**
     * 创建用户订单关联用户信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createOrderUserRelation(OrderUserRelationEditDTO orderUserRelationEditDto, User operatorUser) {
        // 转换为Entity实体
        OrderUserRelation orderUserRelationEntity = orderUserRelationMapper.toEntity(orderUserRelationEditDto);

        // 设置创建信息
        if (CheckTools.isNotNullOrEmpty(operatorUser)) {
            //orderUserRelationEntity.setCreateBy(operatorUser.getId());
            //orderUserRelationEntity.setCreateByName(operatorUser.getUserName());
            //orderUserRelationEntity.setUpdateBy(operatorUser.getId());
            //orderUserRelationEntity.setUpdateByName(operatorUser.getUserName());
        } else {
            throw new BusinessException("用户订单关联用户创建失败：创建人信息获取有误");
        }

        // 创建用户订单关联用户
        orderUserRelationRepository.saveAndFlush(orderUserRelationEntity);
        return orderUserRelationEntity.getOrderId();
    }

    /**
     * 更新用户订单关联用户信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrderUserRelation(OrderUserRelationEditDTO orderUserRelationEditDto, User operatorUser) {
        Optional<OrderUserRelation> optional = orderUserRelationRepository.findById(orderUserRelationEditDto.getUserId());
        if (!optional.isPresent()) {
            throw new BusinessException("用户订单关联用户不存在");
        }

        // 将EditDto属性合并到DB实体中
        OrderUserRelation orderUserRelationDB = optional.get();
        orderUserRelationMapper.merge(orderUserRelationEditDto, orderUserRelationDB);
        // 设置更新信息
        //orderUserRelationDB.setUpdateBy(operatorUser.getId());
        //orderUserRelationDB.setUpdateByName(operatorUser.getUserName());
        // 更新用户订单关联用户
        orderUserRelationRepository.saveAndFlush(orderUserRelationDB);
    }

    /**
     * 删除用户订单关联用户
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteOrderUserRelation(String id, User operatorUser) {
        // before
        deleteById(id);
    }

    /**
     * 批量删除用户订单关联用户
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteOrderUserRelationBatch(List<String> ids, User operatorUser) {
        // 遍历删除
        for (String id : ids) {
            this.deleteOrderUserRelation(id,operatorUser);
        }
    }

    /**
     * 分页查询用户订单关联用户列表
     */
    @Override
    public R<List<OrderUserRelationDTO>> queryOrderUserRelationListByPage(PredicateWrapper predicateWrapper, Pageable pageable) {
        Predicate predicate = predicateWrapper.getPredicate();

        QueryResults<OrderUserRelation> queryResults = getJpaQueryFactory()
                .select(qOrderUserRelation)
                .from(qOrderUserRelation)
                .where(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                //.orderBy(qOrderUserRelation.createTime.desc())
                .fetchResults();

        List<OrderUserRelationDTO> dtoList = queryResults.getResults()
                .stream().map(entity -> getMapper().toBasicDTO(entity))
                .collect(Collectors.toList());
        return R.success(dtoList).setTotal(queryResults.getTotal());
    }

    /**
     * 根据Id查询用户订单关联用户信息
     */
    @Override
    public OrderUserRelationEditDTO queryOrderUserRelationById(String id) {
        Predicate predicate = qOrderUserRelation.orderId.eq(id);
        List<OrderUserRelationEditDTO> queryList = queryOrderUserRelationEditDTOListByPredicate(predicate);
        if (queryList.size() == 1) {
            return queryList.get(0);
        }
        return null;
    }

    /**
     * 导出用户订单关联用户数据
     */
    @Override
    public List<OrderUserRelationDTO> export(PredicateWrapper predicateWrapper) {
        long count = orderUserRelationRepository.count();
        R<List<OrderUserRelationDTO>> exportResult = this.queryOrderUserRelationListByPage(predicateWrapper, PageRequest.of(0, (int) count));
        return exportResult.getData();
    }

    @Override
    public List<OrderUserRelationEditDTO> queryOrderUserRelationEditDTOListByPredicate(Predicate predicate) {
        return getJpaQueryFactory()
                .selectFrom(qOrderUserRelation)
                .where(predicate)
                //.orderBy(qOrderUserRelation.createTime.asc())
                .fetch()
                .stream()
                .map(entity -> getMapper().toEditDTO(entity))
                .collect(Collectors.toList());
    }

    @Override
    public BaseRepository<OrderUserRelation, String> getRepository() {
        return orderUserRelationRepository;
    }

    @Override
    public BaseMapper<OrderUserRelation, OrderUserRelationDTO, OrderUserRelationEditDTO> getMapper() {
        return orderUserRelationMapper;
    }

}
