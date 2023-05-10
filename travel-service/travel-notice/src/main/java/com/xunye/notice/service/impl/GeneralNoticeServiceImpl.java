package com.xunye.notice.service.impl;

import com.xunye.core.result.R;
import com.xunye.core.base.BaseMapper;
import com.xunye.core.base.BaseRepository;
import com.xunye.core.base.BaseServiceImpl;
import com.xunye.core.model.PredicateWrapper;
import com.xunye.notice.dto.GeneralNoticeDTO;
import com.xunye.notice.dto.GeneralNoticeEditDTO;
import com.xunye.notice.entity.GeneralNotice;
import com.xunye.notice.entity.QGeneralNotice;
import com.xunye.notice.mapper.GeneralNoticeMapper;
import com.xunye.notice.repo.GeneralNoticeRepository;
import com.xunye.notice.service.IGeneralNoticeService;
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
public class GeneralNoticeServiceImpl extends BaseServiceImpl<GeneralNoticeEditDTO, GeneralNoticeDTO, GeneralNotice, String> implements IGeneralNoticeService {

    private static final QGeneralNotice qGeneralNotice = QGeneralNotice.generalNotice;

    private final GeneralNoticeMapper generalNoticeMapper;
    private final GeneralNoticeRepository generalNoticeRepository;

    public GeneralNoticeServiceImpl(GeneralNoticeMapper generalNoticeMapper, GeneralNoticeRepository generalNoticeRepository) {
        this.generalNoticeMapper = generalNoticeMapper;
        this.generalNoticeRepository = generalNoticeRepository;
    }

    /**
     * 创建通用注意事项信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createGeneralNotice(GeneralNoticeEditDTO generalNoticeEditDto, User operatorUser) {
        // 转换为Entity实体
        GeneralNotice generalNoticeEntity = generalNoticeMapper.toEntity(generalNoticeEditDto);

        // 设置创建信息
        if (CheckTools.isNotNullOrEmpty(operatorUser)) {
            generalNoticeEntity.setCreateBy(operatorUser.getId());
            generalNoticeEntity.setCreateByName(operatorUser.getUserName());
            generalNoticeEntity.setUpdateBy(operatorUser.getId());
            generalNoticeEntity.setUpdateByName(operatorUser.getUserName());
        } else {
            throw new BusinessException("通用注意事项创建失败：创建人信息获取有误");
        }

        // 创建通用注意事项
        generalNoticeRepository.saveAndFlush(generalNoticeEntity);
        return generalNoticeEntity.getId();
    }

    /**
     * 更新通用注意事项信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateGeneralNotice(GeneralNoticeEditDTO generalNoticeEditDto, User operatorUser) {
        Optional<GeneralNotice> optional = generalNoticeRepository.findById(generalNoticeEditDto.getId());
        if (!optional.isPresent()) {
            throw new BusinessException("通用注意事项不存在");
        }

        // 将EditDto属性合并到DB实体中
        GeneralNotice generalNoticeDB = optional.get();
        generalNoticeMapper.merge(generalNoticeEditDto, generalNoticeDB);
        // 设置更新信息
        generalNoticeDB.setUpdateBy(operatorUser.getId());
        generalNoticeDB.setUpdateByName(operatorUser.getUserName());
        // 更新通用注意事项
        generalNoticeRepository.saveAndFlush(generalNoticeDB);
    }

    /**
     * 删除通用注意事项
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteGeneralNotice(String id, User operatorUser) {
        // before
        deleteById(id);
    }

    /**
     * 批量删除通用注意事项
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteGeneralNoticeBatch(List<String> ids, User operatorUser) {
        // 遍历删除
        for (String id : ids) {
            this.deleteGeneralNotice(id,operatorUser);
        }
    }

    /**
     * 分页查询通用注意事项列表
     */
    @Override
    public R<List<GeneralNoticeDTO>> queryGeneralNoticeListByPage(PredicateWrapper predicateWrapper, Pageable pageable) {
        Predicate predicate = predicateWrapper.getPredicate();

        QueryResults<GeneralNotice> queryResults = getJpaQueryFactory()
                .select(qGeneralNotice)
                .from(qGeneralNotice)
                .where(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qGeneralNotice.createTime.desc())
                .fetchResults();

        List<GeneralNoticeDTO> dtoList = queryResults.getResults()
                .stream().map(entity -> getMapper().toBasicDTO(entity))
                .collect(Collectors.toList());
        return R.success(dtoList).setTotal(queryResults.getTotal());
    }

    /**
     * 根据Id查询通用注意事项信息
     */
    @Override
    public GeneralNoticeEditDTO queryGeneralNoticeById(String id) {
        Predicate predicate = qGeneralNotice.id.eq(id);
        List<GeneralNoticeEditDTO> queryList = queryGeneralNoticeEditDTOListByPredicate(predicate);
        if (queryList.size() == 1) {
            return queryList.get(0);
        }
        return null;
    }

    /**
     * 导出通用注意事项数据
     */
    @Override
    public List<GeneralNoticeDTO> export(PredicateWrapper predicateWrapper) {
        long count = generalNoticeRepository.count();
        R<List<GeneralNoticeDTO>> exportResult = this.queryGeneralNoticeListByPage(predicateWrapper, PageRequest.of(0, (int) count));
        return exportResult.getData();
    }

    @Override
    public List<GeneralNoticeEditDTO> queryGeneralNoticeEditDTOListByPredicate(Predicate predicate) {
        return getJpaQueryFactory()
                .selectFrom(qGeneralNotice)
                .where(predicate)
                .orderBy(qGeneralNotice.createTime.asc())
                .fetch()
                .stream()
                .map(entity -> getMapper().toEditDTO(entity))
                .collect(Collectors.toList());
    }

    @Override
    public BaseRepository<GeneralNotice, String> getRepository() {
        return generalNoticeRepository;
    }

    @Override
    public BaseMapper<GeneralNotice, GeneralNoticeDTO, GeneralNoticeEditDTO> getMapper() {
        return generalNoticeMapper;
    }

}
