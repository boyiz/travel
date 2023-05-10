package com.xunye.route.service.impl;

import com.xunye.core.result.R;
import com.xunye.core.base.BaseMapper;
import com.xunye.core.base.BaseRepository;
import com.xunye.core.base.BaseServiceImpl;
import com.xunye.core.model.PredicateWrapper;
import com.xunye.route.dto.RouteScheduleDTO;
import com.xunye.route.dto.RouteScheduleEditDTO;
import com.xunye.route.entity.RouteSchedule;
import com.xunye.route.entity.QRouteSchedule;
import com.xunye.route.mapper.RouteScheduleMapper;
import com.xunye.route.repo.RouteScheduleRepository;
import com.xunye.route.service.IRouteScheduleService;
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
public class RouteScheduleServiceImpl extends BaseServiceImpl<RouteScheduleEditDTO, RouteScheduleDTO, RouteSchedule, String> implements IRouteScheduleService {

    private static final QRouteSchedule qRouteSchedule = QRouteSchedule.routeSchedule;

    private final RouteScheduleMapper routeScheduleMapper;
    private final RouteScheduleRepository routeScheduleRepository;

    public RouteScheduleServiceImpl(RouteScheduleMapper routeScheduleMapper, RouteScheduleRepository routeScheduleRepository) {
        this.routeScheduleMapper = routeScheduleMapper;
        this.routeScheduleRepository = routeScheduleRepository;
    }

    /**
     * 创建路线-每天计划信息
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createRouteSchedule(RouteScheduleEditDTO routeScheduleEditDto, User operatorUser) {
        // 转换为Entity实体
        RouteSchedule routeScheduleEntity = routeScheduleMapper.toEntity(routeScheduleEditDto);

        // 设置创建信息
        if (CheckTools.isNotNullOrEmpty(operatorUser)) {
            routeScheduleEntity.setCreateBy(operatorUser.getId());
            routeScheduleEntity.setCreateByName(operatorUser.getUserName());
            routeScheduleEntity.setUpdateBy(operatorUser.getId());
            routeScheduleEntity.setUpdateByName(operatorUser.getUserName());
        } else {
            throw new BusinessException("路线-每天计划创建失败：创建人信息获取有误");
        }

        // 创建路线-每天计划
        routeScheduleRepository.saveAndFlush(routeScheduleEntity);
        return routeScheduleEntity.getId();
    }

    /**
     * 更新路线-每天计划信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRouteSchedule(RouteScheduleEditDTO routeScheduleEditDto, User operatorUser) {
        Optional<RouteSchedule> optional = routeScheduleRepository.findById(routeScheduleEditDto.getId());
        if (!optional.isPresent()) {
            throw new BusinessException("路线-每天计划不存在");
        }

        // 将EditDto属性合并到DB实体中
        RouteSchedule routeScheduleDB = optional.get();
        routeScheduleMapper.merge(routeScheduleEditDto, routeScheduleDB);
        // 设置更新信息
        routeScheduleDB.setUpdateBy(operatorUser.getId());
        routeScheduleDB.setUpdateByName(operatorUser.getUserName());
        // 更新路线-每天计划
        routeScheduleRepository.saveAndFlush(routeScheduleDB);
    }

    /**
     * 删除路线-每天计划
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRouteSchedule(String id, User operatorUser) {
        // before
        deleteById(id);
    }

    /**
     * 批量删除路线-每天计划
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRouteScheduleBatch(List<String> ids, User operatorUser) {
        // 遍历删除
        for (String id : ids) {
            this.deleteRouteSchedule(id,operatorUser);
        }
    }

    /**
     * 分页查询路线-每天计划列表
     */
    @Override
    public R<List<RouteScheduleDTO>> queryRouteScheduleListByPage(PredicateWrapper predicateWrapper, Pageable pageable) {
        Predicate predicate = predicateWrapper.getPredicate();

        QueryResults<RouteSchedule> queryResults = getJpaQueryFactory()
                .select(qRouteSchedule)
                .from(qRouteSchedule)
                .where(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qRouteSchedule.createTime.desc())
                .fetchResults();

        List<RouteScheduleDTO> dtoList = queryResults.getResults()
                .stream().map(entity -> getMapper().toBasicDTO(entity))
                .collect(Collectors.toList());
        return R.success(dtoList).setTotal(queryResults.getTotal());
    }

    /**
     * 根据Id查询路线-每天计划信息
     */
    @Override
    public RouteScheduleEditDTO queryRouteScheduleById(String id) {
        Predicate predicate = qRouteSchedule.id.eq(id);
        List<RouteScheduleEditDTO> queryList = queryRouteScheduleEditDTOListByPredicate(predicate);
        if (queryList.size() == 1) {
            return queryList.get(0);
        }
        return null;
    }

    @Override
    public List<RouteScheduleEditDTO> queryRouteScheduleByRouteId(String routeId) {
        return queryRouteScheduleEditDTOListByPredicate(qRouteSchedule.routeId.eq(routeId));
    }

    /**
     * 导出路线-每天计划数据
     */
    @Override
    public List<RouteScheduleDTO> export(PredicateWrapper predicateWrapper) {
        long count = routeScheduleRepository.count();
        R<List<RouteScheduleDTO>> exportResult = this.queryRouteScheduleListByPage(predicateWrapper, PageRequest.of(0, (int) count));
        return exportResult.getData();
    }

    @Override
    public List<RouteScheduleEditDTO> queryRouteScheduleEditDTOListByPredicate(Predicate predicate) {
        return getJpaQueryFactory()
                .selectFrom(qRouteSchedule)
                .where(predicate)
                .orderBy(qRouteSchedule.createTime.asc())
                .fetch()
                .stream()
                .map(entity -> getMapper().toEditDTO(entity))
                .collect(Collectors.toList());
    }

    @Override
    public BaseRepository<RouteSchedule, String> getRepository() {
        return routeScheduleRepository;
    }

    @Override
    public BaseMapper<RouteSchedule, RouteScheduleDTO, RouteScheduleEditDTO> getMapper() {
        return routeScheduleMapper;
    }

}
