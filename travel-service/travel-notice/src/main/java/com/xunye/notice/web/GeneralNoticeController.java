package com.xunye.notice.web;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.xunye.core.result.R;
import com.xunye.core.base.BaseController;
import com.xunye.core.model.PredicateWrapper;
import com.xunye.notice.dto.GeneralNoticeDTO;
import com.xunye.notice.dto.GeneralNoticeEditDTO;
import com.xunye.notice.entity.GeneralNotice;
import com.xunye.notice.service.IGeneralNoticeService;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.web.bind.annotation.*;

import com.xunye.auth.entity.User;
import com.xunye.core.tools.CheckTools;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/notice/general")
public class GeneralNoticeController extends BaseController {

    @Resource
    private IGeneralNoticeService generalNoticeService;

    /**
     * 通用注意事项列表查询接口
     *
     * @param predicate 条件谓语
     * @param pageable  分页参数
     */
    @GetMapping
    public R<List<GeneralNoticeDTO>> list(@QuerydslPredicate(root = GeneralNotice.class) Predicate predicate,
                                         Pageable pageable) {
        return generalNoticeService.queryGeneralNoticeListByPage(PredicateWrapper.of(predicate), pageable);
    }

    /**
     * 通用注意事项创建接口
     *
     * @param addDTO 创建信息
     */
    @PostMapping
    @SaCheckRole("admin")
    public R<String> create(@RequestBody GeneralNoticeEditDTO addDTO) {
        User currentRealUser = new User(new Date());

        // 创建并返回dbId
        String dbId = generalNoticeService.createGeneralNotice(addDTO, currentRealUser);
        return R.success("创建成功",dbId);
    }

    /**
     * 通用注意事项更新接口
     *
     * @param editDto 更新信息
     */
    @PutMapping
    @SaCheckRole("admin")
    public R<Boolean> update(@RequestBody GeneralNoticeEditDTO editDto) {
        // 基础校验
        if (CheckTools.isNullOrEmpty(editDto.getId())) {
            return R.failure("通用注意事项id不存在" );
        }

        User currentRealUser = new User(new Date());

        // 更新通用注意事项
        generalNoticeService.updateGeneralNotice(editDto, currentRealUser);
        return R.success("更新成功", true);
    }

    /**
     * 通用注意事项删除接口
     *
     * @param id ID
     */
    @DeleteMapping("/{id}")
    @SaCheckRole("admin")
    public R<Boolean> delete(@PathVariable String id) {
        // 获取当前操作用户
        User currentRealUser = new User(new Date());
        // 调用service删除
        generalNoticeService.deleteGeneralNotice(id,currentRealUser);
        return R.success("删除成功", true);
    }

    /**
     * 通用注意事项批量删除接口
     *
     * @param ids id集合
     */
    @DeleteMapping("/batch/{ids}")
    @SaCheckRole("admin")
    public R<Boolean> deleteBatch(@PathVariable String ids) {
        // 参数预处理
        if (StrUtil.isEmptyIfStr(ids)) return R.failure();

        // 获取当前操作用户
        User currentRealUser = new User(new Date());
        // 调用service删除
        List<String> idList = Arrays.stream(ids.split(",")).collect(Collectors.toList());
        generalNoticeService.deleteGeneralNoticeBatch(idList,currentRealUser);

        return R.success("批量删除成功", true);
    }

    /**
     * 根据ID查询通用注意事项接口
     *
     * @param id ID
     */
    @GetMapping("/{id}")
    public R<GeneralNoticeEditDTO> queryGeneralNoticeById(@PathVariable String id) {
        GeneralNoticeEditDTO generalNoticeEditDTO = generalNoticeService.queryGeneralNoticeById(id);
        return R.success("查询成功",generalNoticeEditDTO);
    }

    /**
     * 通用注意事项数据导出接口
     *
     * @param predicate 条件谓语
     * @param response  相应
     * @throws IOException IO异常
     */
    @GetMapping("/export")
    @SaCheckRole("admin")
    public void export(@QuerydslPredicate(root = GeneralNotice.class) Predicate predicate, HttpServletResponse response) throws IOException {
        String fileName = "通用注意事项列表导出.xls";
        exportExcelBefore(response, fileName);
        EasyExcel.write(response.getOutputStream(), GeneralNoticeDTO.class)
                .sheet("Sheet1")
                .doWrite(generalNoticeService.export(PredicateWrapper.of(predicate)));
    }
    

}
