package com.xunye.promotion.service.impl;

import com.xunye.core.result.R;
import com.xunye.core.base.BaseMapper;
import com.xunye.core.base.BaseRepository;
import com.xunye.core.base.BaseServiceImpl;
import com.xunye.core.model.PredicateWrapper;
import com.xunye.promotion.dto.CouponsProductRelationDTO;
import com.xunye.promotion.dto.CouponsProductRelationEditDTO;
import com.xunye.promotion.entity.CouponsProductRelation;
import com.xunye.promotion.entity.QCouponsProductRelation;
import com.xunye.promotion.mapper.CouponsProductRelationMapper;
import com.xunye.promotion.repo.CouponsProductRelationRepository;
import com.xunye.promotion.service.ICouponsProductRelationService;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import java.util.Optional;
import com.xunye.core.tools.CheckTools;
import com.xunye.auth.entity.User;
import com.xunye.core.exception.BusinessException;

@Slf4j
@Service
public class CouponsProductRelationServiceImpl extends BaseServiceImpl<CouponsProductRelationEditDTO, CouponsProductRelationDTO, CouponsProductRelation, String> implements ICouponsProductRelationService {

    private static final QCouponsProductRelation qCouponsProductRelation = QCouponsProductRelation.couponsProductRelation;

    private final CouponsProductRelationMapper couponsProductRelationMapper;
    private final CouponsProductRelationRepository couponsProductRelationRepository;

    public CouponsProductRelationServiceImpl(CouponsProductRelationMapper couponsProductRelationMapper, CouponsProductRelationRepository couponsProductRelationRepository) {
        this.couponsProductRelationMapper = couponsProductRelationMapper;
        this.couponsProductRelationRepository = couponsProductRelationRepository;
    }

    /**
     * 创建优惠券-商品 关系表信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createCouponsProductRelation(CouponsProductRelationEditDTO couponsProductRelationEditDto, User operatorUser) {
        // 转换为Entity实体
        CouponsProductRelation couponsProductRelationEntity = couponsProductRelationMapper.toEntity(couponsProductRelationEditDto);

        // 设置创建信息
        if (CheckTools.isNotNullOrEmpty(operatorUser)) {
            couponsProductRelationEntity.setCreateBy(operatorUser.getId());
            couponsProductRelationEntity.setCreateByName(operatorUser.getUserName());
            couponsProductRelationEntity.setUpdateBy(operatorUser.getId());
            couponsProductRelationEntity.setUpdateByName(operatorUser.getUserName());
        } else {
            throw new BusinessException("优惠券-商品 关系表创建失败：创建人信息获取有误");
        }

        // 创建优惠券-商品 关系表
        couponsProductRelationRepository.saveAndFlush(couponsProductRelationEntity);
        return couponsProductRelationEntity.getId();
    }

    /**
     * 更新优惠券-商品 关系表信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCouponsProductRelation(CouponsProductRelationEditDTO couponsProductRelationEditDto, User operatorUser) {
        Optional<CouponsProductRelation> optional = couponsProductRelationRepository.findById(couponsProductRelationEditDto.getId());
        if (!optional.isPresent()) {
            throw new BusinessException("优惠券-商品 关系表不存在");
        }

        // 将EditDto属性合并到DB实体中
        CouponsProductRelation couponsProductRelationDB = optional.get();
        couponsProductRelationMapper.merge(couponsProductRelationEditDto, couponsProductRelationDB);
        // 设置更新信息
        couponsProductRelationDB.setUpdateBy(operatorUser.getId());
        couponsProductRelationDB.setUpdateByName(operatorUser.getUserName());
        // 更新优惠券-商品 关系表
        couponsProductRelationRepository.saveAndFlush(couponsProductRelationDB);
    }

    /**
     * 删除优惠券-商品 关系表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCouponsProductRelation(String id, User operatorUser) {
        // before
        deleteById(id);
    }

    /**
     * 批量删除优惠券-商品 关系表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCouponsProductRelationBatch(List<String> ids, User operatorUser) {
        // 遍历删除
        for (String id : ids) {
            this.deleteCouponsProductRelation(id,operatorUser);
        }
    }

    /**
     * 分页查询优惠券-商品 关系表列表
     */
    //@DataAuth
    @Override
    public R<List<CouponsProductRelationDTO>> queryCouponsProductRelationListByPage(PredicateWrapper predicateWrapper, Pageable pageable) {
        Predicate predicate = predicateWrapper.getPredicate();

        QueryResults<CouponsProductRelation> queryResults = getJpaQueryFactory()
                .select(qCouponsProductRelation)
                .from(qCouponsProductRelation)
                .where(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qCouponsProductRelation.createTime.desc())
                .fetchResults();

        List<CouponsProductRelationDTO> dtoList = queryResults.getResults()
                .stream().map(entity -> getMapper().toBasicDTO(entity))
                .collect(Collectors.toList());
        return R.success(dtoList).setTotal(queryResults.getTotal());
    }

    /**
     * 根据Id查询优惠券-商品 关系表信息
     */
    @Override
    public CouponsProductRelationEditDTO queryCouponsProductRelationById(String id) {
        Predicate predicate = qCouponsProductRelation.id.eq(id);
        List<CouponsProductRelationEditDTO> queryList = queryCouponsProductRelationEditDTOListByPredicate(predicate);
        if (queryList.size() == 1) {
            return queryList.get(0);
        }
        return null;
    }

    /**
     * 导出优惠券-商品 关系表数据
     */
    @Override
    public List<CouponsProductRelationDTO> export(PredicateWrapper predicateWrapper) {
        long count = couponsProductRelationRepository.count();
        R<List<CouponsProductRelationDTO>> exportResult = this.queryCouponsProductRelationListByPage(predicateWrapper, PageRequest.of(0, (int) count));
        return exportResult.getData();
    }

    @Override
    public List<CouponsProductRelationEditDTO> queryCouponsProductRelationEditDTOListByPredicate(Predicate predicate) {
        return getJpaQueryFactory()
                .selectFrom(qCouponsProductRelation)
                .where(predicate)
                .orderBy(qCouponsProductRelation.createTime.asc())
                .fetch()
                .stream()
                .map(entity -> getMapper().toEditDTO(entity))
                .collect(Collectors.toList());
    }

    @Override
    public BaseRepository<CouponsProductRelation, String> getRepository() {
        return couponsProductRelationRepository;
    }

    @Override
    public BaseMapper<CouponsProductRelation, CouponsProductRelationDTO, CouponsProductRelationEditDTO> getMapper() {
        return couponsProductRelationMapper;
    }

}
