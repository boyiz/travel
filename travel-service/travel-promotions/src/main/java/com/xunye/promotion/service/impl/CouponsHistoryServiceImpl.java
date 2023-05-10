package com.xunye.promotion.service.impl;

import com.xunye.core.result.R;
import com.xunye.core.base.BaseMapper;
import com.xunye.core.base.BaseRepository;
import com.xunye.core.base.BaseServiceImpl;
import com.xunye.core.exception.BatchDeleteException;
import com.xunye.core.model.PredicateWrapper;
import com.xunye.core.tools.RedisUtil;
import com.xunye.promotion.dto.CouponsHistoryDTO;
import com.xunye.promotion.dto.CouponsHistoryEditDTO;
import com.xunye.promotion.entity.CouponsHistory;
import com.xunye.promotion.entity.QCouponsHistory;
import com.xunye.promotion.mapper.CouponsHistoryMapper;
import com.xunye.promotion.repo.CouponsHistoryRepository;
import com.xunye.promotion.service.ICouponsHistoryService;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.querydsl.core.types.Predicate;

import javax.annotation.Resource;

import java.util.List;
import java.util.stream.Collectors;

import java.util.Optional;

import com.xunye.core.tools.CheckTools;
import com.xunye.auth.entity.User;
import com.xunye.core.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;

import static com.xunye.common.constant.CouponsConstant.COUPONS_PUBLISH_COUNT;

@Slf4j
@Service
public class CouponsHistoryServiceImpl
    extends BaseServiceImpl<CouponsHistoryEditDTO, CouponsHistoryDTO, CouponsHistory, String>
    implements ICouponsHistoryService {

    private static final QCouponsHistory qCouponsHistory = QCouponsHistory.couponsHistory;

    @Resource
    private RedisUtil redisUtil;

    private final CouponsHistoryMapper couponsHistoryMapper;
    private final CouponsHistoryRepository couponsHistoryRepository;

    public CouponsHistoryServiceImpl(CouponsHistoryMapper couponsHistoryMapper,
        CouponsHistoryRepository couponsHistoryRepository) {
        this.couponsHistoryMapper = couponsHistoryMapper;
        this.couponsHistoryRepository = couponsHistoryRepository;
    }

    /**
     * 创建优惠券历史记录信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createCouponsHistory(CouponsHistoryEditDTO couponsHistoryEditDto, User operatorUser) {
        // 转换为Entity实体
        CouponsHistory couponsHistoryEntity = couponsHistoryMapper.toEntity(couponsHistoryEditDto);

        // 设置创建信息
        if (CheckTools.isNotNullOrEmpty(operatorUser)) {
            couponsHistoryEntity.setCreateBy(operatorUser.getId());
            couponsHistoryEntity.setCreateByName(operatorUser.getUserName());
            couponsHistoryEntity.setUpdateBy(operatorUser.getId());
            couponsHistoryEntity.setUpdateByName(operatorUser.getUserName());
        } else {
            throw new BusinessException("优惠券历史记录创建失败：创建人信息获取有误");
        }

        // 创建优惠券历史记录
        couponsHistoryRepository.saveAndFlush(couponsHistoryEntity);
        // redis 库存减一
        long l = redisUtil.decrByUntilTo0(COUPONS_PUBLISH_COUNT + couponsHistoryEditDto.getCouponId(), 1);

        if (l == 0) {
            throw new BusinessException("优惠券领取失败，请重新领取");
        }

        return couponsHistoryEntity.getId();
    }

    /**
     * 更新优惠券历史记录信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCouponsHistory(CouponsHistoryEditDTO couponsHistoryEditDto, User operatorUser) {
        Optional<CouponsHistory> optional = couponsHistoryRepository.findById(couponsHistoryEditDto.getId());
        if (!optional.isPresent()) {
            throw new BusinessException("优惠券历史记录不存在");
        }

        // 将EditDto属性合并到DB实体中
        CouponsHistory couponsHistoryDB = optional.get();
        couponsHistoryMapper.merge(couponsHistoryEditDto, couponsHistoryDB);
        // 设置更新信息
        couponsHistoryDB.setUpdateBy(operatorUser.getId());
        couponsHistoryDB.setUpdateByName(operatorUser.getUserName());
        // 更新优惠券历史记录
        couponsHistoryRepository.saveAndFlush(couponsHistoryDB);
    }

    /**
     * 删除优惠券历史记录
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCouponsHistory(String id, User operatorUser) {
        // before
        deleteById(id);
    }

    /**
     * 批量删除优惠券历史记录
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCouponsHistoryBatch(List<String> ids, User operatorUser) {
        // 遍历删除
        for (String id : ids) {
            this.deleteCouponsHistory(id, operatorUser);
        }
    }

    /**
     * 分页查询优惠券历史记录列表
     */
    //@DataAuth
    @Override
    public R<List<CouponsHistoryDTO>> queryCouponsHistoryListByPage(PredicateWrapper predicateWrapper,
        Pageable pageable) {
        Predicate predicate = predicateWrapper.getPredicate();

        QueryResults<CouponsHistory> queryResults = getJpaQueryFactory()
            .select(qCouponsHistory)
            .from(qCouponsHistory)
            .where(predicate)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(qCouponsHistory.createTime.desc())
            .fetchResults();

        List<CouponsHistoryDTO> dtoList = queryResults.getResults()
            .stream().map(entity -> getMapper().toBasicDTO(entity))
            .collect(Collectors.toList());
        return R.success(dtoList).setTotal(queryResults.getTotal());
    }

    /**
     * 根据Id查询优惠券历史记录信息
     */
    @Override
    public CouponsHistoryEditDTO queryCouponsHistoryById(String id) {
        Predicate predicate = qCouponsHistory.id.eq(id);
        List<CouponsHistoryEditDTO> queryList = queryCouponsHistoryEditDTOListByPredicate(predicate);
        if (queryList.size() == 1) {
            return queryList.get(0);
        }
        return null;
    }

    /**
     * 导出优惠券历史记录数据
     */
    @Override
    public List<CouponsHistoryDTO> export(PredicateWrapper predicateWrapper) {
        long count = couponsHistoryRepository.count();
        R<List<CouponsHistoryDTO>> exportResult = this.queryCouponsHistoryListByPage(predicateWrapper,
            PageRequest.of(0, (int)count));
        return exportResult.getData();
    }

    @Override
    public List<CouponsHistoryEditDTO> queryCouponsHistoryEditDTOListByPredicate(Predicate predicate) {
        return getJpaQueryFactory()
            .selectFrom(qCouponsHistory)
            .where(predicate)
            .orderBy(qCouponsHistory.createTime.asc())
            .fetch()
            .stream()
            .map(entity -> getMapper().toEditDTO(entity))
            .collect(Collectors.toList());
    }

    @Override
    public BaseRepository<CouponsHistory, String> getRepository() {
        return couponsHistoryRepository;
    }

    @Override
    public BaseMapper<CouponsHistory, CouponsHistoryDTO, CouponsHistoryEditDTO> getMapper() {
        return couponsHistoryMapper;
    }

}
