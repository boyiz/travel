package com.xunye.route.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Predicate;
import com.xunye.auth.entity.User;
import com.xunye.common.constant.RouteConstant;
import com.xunye.core.base.BaseMapper;
import com.xunye.core.base.BaseRepository;
import com.xunye.core.base.BaseServiceImpl;
import com.xunye.core.exception.BusinessException;
import com.xunye.core.model.PredicateWrapper;
import com.xunye.core.tools.CheckTools;
import com.xunye.core.tools.RedisUtil;
import com.xunye.group.dto.GroupInfoDTO;
import com.xunye.group.entity.GroupInfo;
import com.xunye.group.entity.QGroupInfo;
import com.xunye.group.service.IGroupInfoService;
import com.xunye.notice.dto.GeneralNoticeDTO;
import com.xunye.notice.dto.RouteNoticeDTO;
import com.xunye.notice.entity.GeneralNotice;
import com.xunye.notice.entity.QGeneralNotice;
import com.xunye.notice.entity.QRouteNotice;
import com.xunye.notice.entity.RouteNotice;
import com.xunye.notice.service.IGeneralNoticeService;
import com.xunye.notice.service.IRouteNoticeService;
import com.xunye.route.dto.RouteImageDTO;
import com.xunye.route.dto.RouteInfoDTO;
import com.xunye.route.dto.RouteInfoEditDTO;
import com.xunye.route.dto.RouteScheduleDTO;
import com.xunye.route.entity.QRouteImage;
import com.xunye.route.entity.QRouteInfo;
import com.xunye.route.entity.QRouteSchedule;
import com.xunye.route.entity.RouteImage;
import com.xunye.route.entity.RouteInfo;
import com.xunye.route.entity.RouteSchedule;
import com.xunye.route.mapper.RouteInfoMapper;
import com.xunye.route.repo.RouteInfoRepository;
import com.xunye.route.service.IRouteImageService;
import com.xunye.route.service.IRouteInfoService;
import com.xunye.route.service.IRouteScheduleService;
import com.xunye.route.vo.RouteDetailVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class RouteInfoServiceImpl extends BaseServiceImpl<RouteInfoEditDTO, RouteInfoDTO, RouteInfo, String>
    implements IRouteInfoService {

    private static final QRouteInfo qRouteInfo = QRouteInfo.routeInfo;

    @Resource
    private RedisUtil redisUtil;

    private final RouteInfoMapper routeInfoMapper;
    private final RouteInfoRepository routeInfoRepository;
    private final IGroupInfoService groupInfoService;
    private final IRouteScheduleService routeScheduleService;
    private final IRouteImageService routeImageService;
    private final IRouteNoticeService routeNoticeService;
    private final IGeneralNoticeService generalNoticeService;


    public RouteInfoServiceImpl(RouteInfoMapper routeInfoMapper, RouteInfoRepository routeInfoRepository,
        IGroupInfoService groupInfoService, IRouteScheduleService routeScheduleService,
        IRouteImageService routeImageService, IRouteNoticeService routeNoticeService,
        IGeneralNoticeService generalNoticeService) {
        this.routeInfoMapper = routeInfoMapper;
        this.routeInfoRepository = routeInfoRepository;
        this.groupInfoService = groupInfoService;
        this.routeScheduleService = routeScheduleService;
        this.routeImageService = routeImageService;
        this.routeNoticeService = routeNoticeService;
        this.generalNoticeService = generalNoticeService;
    }

    /**
     * 创建路线信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createRouteInfo(RouteInfoEditDTO routeInfoEditDto, User operatorUser) {
        // 转换为Entity实体
        RouteInfo routeInfoEntity = routeInfoMapper.toEntity(routeInfoEditDto);

        // 设置创建信息
        if (CheckTools.isNotNullOrEmpty(operatorUser)) {
            routeInfoEntity.setCreateBy(operatorUser.getId());
            routeInfoEntity.setCreateByName(operatorUser.getUserName());
            routeInfoEntity.setUpdateBy(operatorUser.getId());
            routeInfoEntity.setUpdateByName(operatorUser.getUserName());
        } else {
            throw new BusinessException("路线创建失败：创建人信息获取有误");
        }

        // 创建路线
        routeInfoRepository.saveAndFlush(routeInfoEntity);
        return routeInfoEntity.getId().toString();
    }

    /**
     * 更新路线信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRouteInfo(RouteInfoEditDTO routeInfoEditDto, User operatorUser) {
        Optional<RouteInfo> optional = routeInfoRepository.findById(routeInfoEditDto.getId());
        if (!optional.isPresent()) {
            throw new BusinessException("路线不存在");
        }
        // update 移除redis 缓存值
        redisUtil.setRemove(RouteConstant.ROUTE_PREX + routeInfoEditDto.getId());

        // 将EditDto属性合并到DB实体中
        RouteInfo routeInfoDB = optional.get();
        routeInfoMapper.merge(routeInfoEditDto, routeInfoDB);
        // 设置更新信息
        routeInfoDB.setUpdateBy(operatorUser.getId());
        routeInfoDB.setUpdateByName(operatorUser.getUserName());
        // 更新路线
        routeInfoRepository.saveAndFlush(routeInfoDB);
    }

    /**
     * 删除路线
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRouteInfo(String id, User operatorUser) {
        // before
        deleteById(id);
    }

    /**
     * 批量删除路线
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRouteInfoBatch(List<String> ids, User operatorUser) {
        // 遍历删除
        for (String id : ids) {
            this.deleteRouteInfo(id, operatorUser);
        }
    }

    @Override
    public RouteDetailVo queryRouteInfoByRouteId(String routeId) {
        Pageable pageable = PageRequest.of(0, 1);
        PredicateWrapper predicateWrapper = new PredicateWrapper(qRouteInfo.id.eq(routeId),"");

        List<RouteDetailVo> routeDetailVoList = queryRouteInfoListByPage(predicateWrapper, pageable);
        if (routeDetailVoList.size() > 0) {
            // redis来一份
            redisUtil.set(RouteConstant.ROUTE_PREX + routeId,routeDetailVoList.get(0));
            return routeDetailVoList.get(0);
        }
        return null;
    }

    /**
     * 分页查询路线列表
     */
    @Override
    public List<RouteDetailVo> queryRouteInfoListByPage(PredicateWrapper predicateWrapper, Pageable pageable) {

        Predicate predicate = predicateWrapper.getPredicate();
        // 连表查询
        QGroupInfo qGroupInfo = QGroupInfo.groupInfo;
        QRouteNotice qRouteNotice = QRouteNotice.routeNotice;
        QGeneralNotice qGeneralNotice = QGeneralNotice.generalNotice;
        QRouteSchedule qRouteSchedule = QRouteSchedule.routeSchedule;
        QRouteImage qRouteImage = QRouteImage.routeImage;
        QueryResults<RouteInfo> queryResults = getJpaQueryFactory()
            //.select(qRouteInfo, qGroupInfo, qRouteNotice)
            .select(qRouteInfo)
            .from(qRouteInfo)
            //.leftJoin(qGroupInfo).on(qGroupInfo.routeId.eq(qRouteInfo.id))
            //.leftJoin(qRouteNotice).on(qRouteNotice.routeId.eq(qRouteInfo.id))
            .where(predicate)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(qRouteInfo.hot.asc())
            .orderBy(qRouteInfo.sortRule.asc())
            .fetchResults();

        List<RouteInfoDTO> dtoList = queryResults.getResults()
            .stream().map(entity -> getMapper().toBasicDTO(entity))
            .collect(Collectors.toList());

        List<RouteDetailVo> collect = dtoList.stream().map(
            routeInfoDTO -> {
                RouteDetailVo routeDetailVo = new RouteDetailVo();
                // 开团
                QueryResults<GroupInfo> groupInfoQueryResults = getJpaQueryFactory()
                    .select(qGroupInfo)
                    .from(qGroupInfo)
                    .where(qGroupInfo.routeId.eq(routeInfoDTO.getId()))
                    .fetchResults();
                List<GroupInfoDTO> groupInfoDTOS = groupInfoQueryResults.getResults().stream().map(
                    entity -> groupInfoService.getMapper().toBasicDTO(entity)).collect(
                    Collectors.toList());

                // 路线安排
                QueryResults<RouteSchedule> routeScheduleQueryResults = getJpaQueryFactory()
                    .select(qRouteSchedule)
                    .from(qRouteSchedule)
                    .where(qRouteSchedule.routeId.eq(routeInfoDTO.getId()))
                    .fetchResults();
                List<RouteScheduleDTO> routeScheduleDTOS = routeScheduleQueryResults.getResults().stream().map(
                    entity -> routeScheduleService.getMapper().toBasicDTO(entity)).collect(
                    Collectors.toList());

                // 图片
                QueryResults<RouteImage>  routeImageQueryResults = getJpaQueryFactory()
                    .select(qRouteImage)
                    .from(qRouteImage)
                    .where(qRouteImage.routeId.eq(routeInfoDTO.getId()))
                    .fetchResults();
                List<RouteImageDTO> routeImageDTOS = routeImageQueryResults.getResults().stream().map(
                    entity -> routeImageService.getMapper().toBasicDTO(entity)).collect(
                    Collectors.toList());

                // 路线提示信息
                QueryResults<RouteNotice> routeNoticeQueryResults = getJpaQueryFactory()
                    .select(qRouteNotice)
                    .from(qRouteNotice)
                    .where(qRouteNotice.routeId.eq(routeInfoDTO.getId()))
                    .fetchResults();
                List<RouteNoticeDTO> routeNoticeDTOS = routeNoticeQueryResults.getResults().stream().map(
                    entity -> routeNoticeService.getMapper().toBasicDTO(entity)).collect(
                    Collectors.toList());

                // 通用提示信息
                QueryResults<GeneralNotice> generalNoticeQueryResults = getJpaQueryFactory()
                    .select(qGeneralNotice)
                    .from(qGeneralNotice)
                    .fetchResults();
                List<GeneralNoticeDTO> generalNoticeDTOS = generalNoticeQueryResults.getResults().stream().map(
                    entity -> generalNoticeService.getMapper().toBasicDTO(entity)).collect(
                    Collectors.toList());

                if (generalNoticeDTOS.size() > 0) {
                    routeDetailVo.setGeneralNotice(generalNoticeDTOS.get(0));
                }
                if (routeNoticeDTOS.size() > 0) {
                    routeDetailVo.setRouteNotice(routeNoticeDTOS.get(0));
                }
                routeDetailVo.setRouteImageList(routeImageDTOS);
                routeDetailVo.setRouteScheduleList(routeScheduleDTOS);
                routeDetailVo.setGroupInfo(groupInfoDTOS);
                routeDetailVo.setRouteInfo(routeInfoDTO);

                return routeDetailVo;
            }
        ).collect(Collectors.toList());
        return collect;
    }

    /**
     * 根据Id查询路线信息
     */
    @Override
    public RouteInfoEditDTO queryRouteInfoById(String id) {
        Predicate predicate = qRouteInfo.id.eq(id);
        List<RouteInfoEditDTO> queryList = queryRouteInfoEditDTOListByPredicate(predicate);
        if (queryList.size() == 1) {
            return queryList.get(0);
        }
        return null;
    }

    /**
     * 导出路线数据
     */
    @Override
    public List<RouteDetailVo> export(PredicateWrapper predicateWrapper) {
        long count = routeInfoRepository.count();
        return this.queryRouteInfoListByPage(predicateWrapper,
            PageRequest.of(0, (int)count));
    }

    @Override
    public List<RouteInfoEditDTO> queryRouteInfoEditDTOListByPredicate(Predicate predicate) {
        return getJpaQueryFactory()
            .selectFrom(qRouteInfo)
            .where(predicate)
            .orderBy(qRouteInfo.createTime.asc())
            .fetch()
            .stream()
            .map(entity -> getMapper().toEditDTO(entity))
            .collect(Collectors.toList());
    }

    @Override
    public BaseRepository<RouteInfo, String> getRepository() {
        return routeInfoRepository;
    }

    @Override
    public BaseMapper<RouteInfo, RouteInfoDTO, RouteInfoEditDTO> getMapper() {
        return routeInfoMapper;
    }

}
