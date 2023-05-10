package com.xunye.route.service.impl;

import com.xunye.core.result.R;
import com.xunye.core.base.BaseMapper;
import com.xunye.core.base.BaseRepository;
import com.xunye.core.base.BaseServiceImpl;
import com.xunye.core.exception.BatchDeleteException;
import com.xunye.core.model.PredicateWrapper;
import com.xunye.route.dto.RouteImageDTO;
import com.xunye.route.dto.RouteImageEditDTO;
import com.xunye.route.entity.RouteImage;
import com.xunye.route.entity.QRouteImage;
import com.xunye.route.mapper.RouteImageMapper;
import com.xunye.route.repo.RouteImageRepository;
import com.xunye.route.service.IRouteImageService;
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


@Slf4j
@Service
public class RouteImageServiceImpl extends BaseServiceImpl<RouteImageEditDTO, RouteImageDTO, RouteImage, String> implements IRouteImageService {

    private static final QRouteImage qRouteImage = QRouteImage.routeImage;

    private final RouteImageMapper routeImageMapper;
    private final RouteImageRepository routeImageRepository;

    public RouteImageServiceImpl(RouteImageMapper routeImageMapper, RouteImageRepository routeImageRepository) {
        this.routeImageMapper = routeImageMapper;
        this.routeImageRepository = routeImageRepository;
    }

    /**
     * 创建路线介绍图信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createRouteImage(RouteImageEditDTO routeImageEditDto, User operatorUser) {
        // 转换为Entity实体
        RouteImage routeImageEntity = routeImageMapper.toEntity(routeImageEditDto);

        // 设置创建信息
        if (CheckTools.isNotNullOrEmpty(operatorUser)) {
            routeImageEntity.setCreateBy(operatorUser.getId());
            routeImageEntity.setCreateByName(operatorUser.getUserName());
            routeImageEntity.setUpdateBy(operatorUser.getId());
            routeImageEntity.setUpdateByName(operatorUser.getUserName());
        } else {
            throw new BusinessException("路线介绍图创建失败：创建人信息获取有误");
        }

        // 创建路线介绍图
        routeImageRepository.saveAndFlush(routeImageEntity);
        return routeImageEntity.getId();
    }

    /**
     * 更新路线介绍图信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRouteImage(RouteImageEditDTO routeImageEditDto, User operatorUser) {
        Optional<RouteImage> optional = routeImageRepository.findById(routeImageEditDto.getId());
        if (!optional.isPresent()) {
            throw new BusinessException("路线介绍图不存在");
        }

        // 将EditDto属性合并到DB实体中
        RouteImage routeImageDB = optional.get();
        routeImageMapper.merge(routeImageEditDto, routeImageDB);
        // 设置更新信息
        routeImageDB.setUpdateBy(operatorUser.getId());
        routeImageDB.setUpdateByName(operatorUser.getUserName());
        // 更新路线介绍图
        routeImageRepository.saveAndFlush(routeImageDB);
    }

    /**
     * 删除路线介绍图
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRouteImage(String id, User operatorUser) {
        // before
        deleteById(id);
    }

    /**
     * 批量删除路线介绍图
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRouteImageBatch(List<String> ids, User operatorUser) {
        // 遍历删除
        for (String id : ids) {
            this.deleteRouteImage(id,operatorUser);
        }
    }

    /**
     * 分页查询路线介绍图列表
     */
    @Override
    public R<List<RouteImageDTO>> queryRouteImageListByPage(PredicateWrapper predicateWrapper, Pageable pageable) {
        Predicate predicate = predicateWrapper.getPredicate();

        QueryResults<RouteImage> queryResults = getJpaQueryFactory()
                .select(qRouteImage)
                .from(qRouteImage)
                .where(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qRouteImage.createTime.desc())
                .fetchResults();

        List<RouteImageDTO> dtoList = queryResults.getResults()
                .stream().map(entity -> getMapper().toBasicDTO(entity))
                .collect(Collectors.toList());
        return R.success(dtoList).setTotal(queryResults.getTotal());
    }

    /**
     * 根据Id查询路线介绍图信息
     */
    @Override
    public RouteImageEditDTO queryRouteImageById(String id) {
        Predicate predicate = qRouteImage.id.eq(id);
        List<RouteImageEditDTO> queryList = queryRouteImageEditDTOListByPredicate(predicate);
        if (queryList.size() == 1) {
            return queryList.get(0);
        }
        return null;
    }

    /**
     * 导出路线介绍图数据
     */
    @Override
    public List<RouteImageDTO> export(PredicateWrapper predicateWrapper) {
        long count = routeImageRepository.count();
        R<List<RouteImageDTO>> exportResult = this.queryRouteImageListByPage(predicateWrapper, PageRequest.of(0, (int) count));
        return exportResult.getData();
    }

    @Override
    public List<RouteImageEditDTO> queryRouteImageEditDTOListByPredicate(Predicate predicate) {
        return getJpaQueryFactory()
                .selectFrom(qRouteImage)
                .where(predicate)
                .orderBy(qRouteImage.createTime.asc())
                .fetch()
                .stream()
                .map(entity -> getMapper().toEditDTO(entity))
                .collect(Collectors.toList());
    }

    @Override
    public BaseRepository<RouteImage, String> getRepository() {
        return routeImageRepository;
    }

    @Override
    public BaseMapper<RouteImage, RouteImageDTO, RouteImageEditDTO> getMapper() {
        return routeImageMapper;
    }

}
