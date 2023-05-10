package com.xunye.notice.service.impl;

import com.xunye.core.result.R;
import com.xunye.core.base.BaseMapper;
import com.xunye.core.base.BaseRepository;
import com.xunye.core.base.BaseServiceImpl;
import com.xunye.core.model.PredicateWrapper;
import com.xunye.notice.dto.RouteNoticeDTO;
import com.xunye.notice.dto.RouteNoticeEditDTO;
import com.xunye.notice.entity.RouteNotice;
import com.xunye.notice.entity.QRouteNotice;
import com.xunye.notice.mapper.RouteNoticeMapper;
import com.xunye.notice.repo.RouteNoticeRepository;
import com.xunye.notice.service.IRouteNoticeService;
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


@Slf4j
@Service
public class RouteNoticeServiceImpl extends BaseServiceImpl<RouteNoticeEditDTO, RouteNoticeDTO, RouteNotice, String> implements IRouteNoticeService {

    private static final QRouteNotice qRouteNotice = QRouteNotice.routeNotice;

    private final RouteNoticeMapper routeNoticeMapper;
    private final RouteNoticeRepository routeNoticeRepository;

    public RouteNoticeServiceImpl(RouteNoticeMapper routeNoticeMapper, RouteNoticeRepository routeNoticeRepository) {
        this.routeNoticeMapper = routeNoticeMapper;
        this.routeNoticeRepository = routeNoticeRepository;
    }

    /**
     * 创建路线注意事项信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createRouteNotice(RouteNoticeEditDTO routeNoticeEditDto, User operatorUser) {
        // 转换为Entity实体
        RouteNotice routeNoticeEntity = routeNoticeMapper.toEntity(routeNoticeEditDto);

        // 设置创建信息
        if (CheckTools.isNotNullOrEmpty(operatorUser)) {
            routeNoticeEntity.setCreateBy(operatorUser.getId());
            routeNoticeEntity.setCreateByName(operatorUser.getUserName());
            routeNoticeEntity.setUpdateBy(operatorUser.getId());
            routeNoticeEntity.setUpdateByName(operatorUser.getUserName());
        } else {
            throw new BusinessException("路线注意事项创建失败：创建人信息获取有误");
        }

        // 创建路线注意事项
        routeNoticeRepository.saveAndFlush(routeNoticeEntity);
        return routeNoticeEntity.getId();
    }

    /**
     * 更新路线注意事项信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRouteNotice(RouteNoticeEditDTO routeNoticeEditDto, User operatorUser) {
        Optional<RouteNotice> optional = routeNoticeRepository.findById(routeNoticeEditDto.getId());
        if (!optional.isPresent()) {
            throw new BusinessException("路线注意事项不存在");
        }

        // 将EditDto属性合并到DB实体中
        RouteNotice routeNoticeDB = optional.get();
        routeNoticeMapper.merge(routeNoticeEditDto, routeNoticeDB);
        // 设置更新信息
        routeNoticeDB.setUpdateBy(operatorUser.getId());
        routeNoticeDB.setUpdateByName(operatorUser.getUserName());
        // 更新路线注意事项
        routeNoticeRepository.saveAndFlush(routeNoticeDB);
    }

    /**
     * 删除路线注意事项
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRouteNotice(String id, User operatorUser) {
        // before
        deleteById(id);
    }

    /**
     * 批量删除路线注意事项
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRouteNoticeBatch(List<String> ids, User operatorUser) {
        // 遍历删除
        for (String id : ids) {
            this.deleteRouteNotice(id,operatorUser);
        }
    }

    /**
     * 分页查询路线注意事项列表
     */
    @Override
    public R<List<RouteNoticeDTO>> queryRouteNoticeListByPage(PredicateWrapper predicateWrapper, Pageable pageable) {
        Predicate predicate = predicateWrapper.getPredicate();

        QueryResults<RouteNotice> queryResults = getJpaQueryFactory()
                .select(qRouteNotice)
                .from(qRouteNotice)
                .where(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qRouteNotice.createTime.desc())
                .fetchResults();

        List<RouteNoticeDTO> dtoList = queryResults.getResults()
                .stream().map(entity -> getMapper().toBasicDTO(entity))
                .collect(Collectors.toList());
        return R.success(dtoList).setTotal(queryResults.getTotal());
    }

    /**
     * 根据Id查询路线注意事项信息
     */
    @Override
    public RouteNoticeEditDTO queryRouteNoticeById(String id) {
        Predicate predicate = qRouteNotice.id.eq(id);
        List<RouteNoticeEditDTO> queryList = queryRouteNoticeEditDTOListByPredicate(predicate);
        if (queryList.size() == 1) {
            return queryList.get(0);
        }
        return null;
    }

    /**
     * 导出路线注意事项数据
     */
    @Override
    public List<RouteNoticeDTO> export(PredicateWrapper predicateWrapper) {
        long count = routeNoticeRepository.count();
        R<List<RouteNoticeDTO>> exportResult = this.queryRouteNoticeListByPage(predicateWrapper, PageRequest.of(0, (int) count));
        return exportResult.getData();
    }

    @Override
    public List<RouteNoticeEditDTO> queryRouteNoticeEditDTOListByPredicate(Predicate predicate) {
        return getJpaQueryFactory()
                .selectFrom(qRouteNotice)
                .where(predicate)
                .orderBy(qRouteNotice.createTime.asc())
                .fetch()
                .stream()
                .map(entity -> getMapper().toEditDTO(entity))
                .collect(Collectors.toList());
    }

    @Override
    public RouteNoticeEditDTO queryRouteNoticeByRouteId(String routeId) {
        Predicate predicate = qRouteNotice.routeId.eq(routeId);
        List<RouteNoticeEditDTO> queryList = queryRouteNoticeEditDTOListByPredicate(predicate);
        if (queryList.size() == 1) {
            return queryList.get(0);
        }
        return null;
    }

    @Override
    public BaseRepository<RouteNotice, String> getRepository() {
        return routeNoticeRepository;
    }

    @Override
    public BaseMapper<RouteNotice, RouteNoticeDTO, RouteNoticeEditDTO> getMapper() {
        return routeNoticeMapper;
    }

}
