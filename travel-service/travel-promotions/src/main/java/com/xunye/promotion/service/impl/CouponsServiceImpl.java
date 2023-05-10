package com.xunye.promotion.service.impl;

import com.xunye.core.result.R;
import com.xunye.core.base.BaseMapper;
import com.xunye.core.base.BaseRepository;
import com.xunye.core.base.BaseServiceImpl;
import com.xunye.core.exception.BatchDeleteException;
import com.xunye.core.model.PredicateWrapper;
import com.xunye.core.tools.RedisUtil;
import com.xunye.promotion.dto.CouponsDTO;
import com.xunye.promotion.dto.CouponsEditDTO;
import com.xunye.promotion.entity.Coupons;
import com.xunye.promotion.entity.QCoupons;
import com.xunye.promotion.mapper.CouponsMapper;
import com.xunye.promotion.repo.CouponsRepository;
import com.xunye.promotion.service.ICouponsService;
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
public class CouponsServiceImpl extends BaseServiceImpl<CouponsEditDTO, CouponsDTO, Coupons, String> implements ICouponsService {

    private static final QCoupons qCoupons = QCoupons.coupons;

    @Resource
    private RedisUtil redisUtil;

    private final CouponsMapper couponsMapper;
    private final CouponsRepository couponsRepository;

    public CouponsServiceImpl(CouponsMapper couponsMapper, CouponsRepository couponsRepository) {
        this.couponsMapper = couponsMapper;
        this.couponsRepository = couponsRepository;
    }

    /**
     * 创建优惠券信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createCoupons(CouponsEditDTO couponsEditDto, User operatorUser) {
        // 转换为Entity实体
        Coupons couponsEntity = couponsMapper.toEntity(couponsEditDto);

        // 设置创建信息
        if (CheckTools.isNotNullOrEmpty(operatorUser)) {
            couponsEntity.setCreateBy(operatorUser.getId());
            couponsEntity.setCreateByName(operatorUser.getUserName());
            couponsEntity.setUpdateBy(operatorUser.getId());
            couponsEntity.setUpdateByName(operatorUser.getUserName());
        } else {
            throw new BusinessException("优惠券创建失败：创建人信息获取有误");
        }

        // 创建优惠券
        couponsRepository.saveAndFlush(couponsEntity);

        // redis 缓存数量
        redisUtil.set(COUPONS_PUBLISH_COUNT + couponsEntity.getId(), couponsEntity.getPublishCount());

        return couponsEntity.getId();
    }

    /**
     * 更新优惠券信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCoupons(CouponsEditDTO couponsEditDto, User operatorUser) {
        Optional<Coupons> optional = couponsRepository.findById(couponsEditDto.getId());
        if (!optional.isPresent()) {
            throw new BusinessException("优惠券不存在");
        }

        // 将EditDto属性合并到DB实体中
        Coupons couponsDB = optional.get();
        couponsMapper.merge(couponsEditDto, couponsDB);
        // 设置更新信息
        couponsDB.setUpdateBy(operatorUser.getId());
        couponsDB.setUpdateByName(operatorUser.getUserName());
        // 更新优惠券
        couponsRepository.saveAndFlush(couponsDB);
    }

    /**
     * 删除优惠券
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCoupons(String id, User operatorUser) {
        // before
        deleteById(id);
    }

    /**
     * 批量删除优惠券
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCouponsBatch(List<String> ids, User operatorUser) {
        // 遍历删除
        for (String id : ids) {
            this.deleteCoupons(id,operatorUser);
        }
    }

    /**
     * 分页查询优惠券列表
     */
    //@DataAuth
    @Override
    public R<List<CouponsDTO>> queryCouponsListByPage(PredicateWrapper predicateWrapper, Pageable pageable) {
        Predicate predicate = predicateWrapper.getPredicate();

        QueryResults<Coupons> queryResults = getJpaQueryFactory()
                .select(qCoupons)
                .from(qCoupons)
                .where(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qCoupons.createTime.desc())
                .fetchResults();

        List<CouponsDTO> dtoList = queryResults.getResults()
                .stream().map(entity -> getMapper().toBasicDTO(entity))
                .collect(Collectors.toList());
        return R.success(dtoList).setTotal(queryResults.getTotal());
    }

    /**
     * 根据Id查询优惠券信息
     */
    @Override
    public CouponsEditDTO queryCouponsById(String id) {
        Predicate predicate = qCoupons.id.eq(id);
        List<CouponsEditDTO> queryList = queryCouponsEditDTOListByPredicate(predicate);
        if (queryList.size() == 1) {
            return queryList.get(0);
        }
        return null;
    }

    /**
     * 导出优惠券数据
     */
    @Override
    public List<CouponsDTO> export(PredicateWrapper predicateWrapper) {
        long count = couponsRepository.count();
        R<List<CouponsDTO>> exportResult = this.queryCouponsListByPage(predicateWrapper, PageRequest.of(0, (int) count));
        return exportResult.getData();
    }

    @Override
    public List<CouponsEditDTO> queryCouponsEditDTOListByPredicate(Predicate predicate) {
        return getJpaQueryFactory()
                .selectFrom(qCoupons)
                .where(predicate)
                .orderBy(qCoupons.createTime.asc())
                .fetch()
                .stream()
                .map(entity -> getMapper().toEditDTO(entity))
                .collect(Collectors.toList());
    }

    @Override
    public BaseRepository<Coupons, String> getRepository() {
        return couponsRepository;
    }

    @Override
    public BaseMapper<Coupons, CouponsDTO, CouponsEditDTO> getMapper() {
        return couponsMapper;
    }

}
