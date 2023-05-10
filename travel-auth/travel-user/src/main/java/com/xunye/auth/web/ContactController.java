package com.xunye.auth.web;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.xunye.auth.entity.QContact;
import com.xunye.core.result.R;
//import com.xunye.core.annotation.ApiAuth;
import com.xunye.core.base.BaseController;
import com.xunye.core.model.PredicateWrapper;
import com.xunye.auth.dto.ContactDTO;
import com.xunye.auth.dto.ContactEditDTO;
import com.xunye.auth.entity.Contact;
import com.xunye.auth.service.IContactService;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.xunye.auth.entity.User;
import com.xunye.core.tools.CheckTools;
import com.xunye.auth.tool.SecurityTools;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import java.util.Map;
import java.util.HashMap;

@RestController
@SaCheckLogin
@RequestMapping("/user/contact")
public class ContactController extends BaseController {

    @Resource
    private IContactService contactService;
    @Resource
    private SecurityTools securityTools;
    /**
     * 关联出行人列表查询接口
     *
     * @param predicate 条件谓语
     * @param pageable  分页参数
     */
    @GetMapping("/all")
    @SaCheckRole("admin")
    public R<List<ContactDTO>> allList(@QuerydslPredicate(root = Contact.class) Predicate predicate,
                                         Pageable pageable) {
        return contactService.queryContactListByPage(PredicateWrapper.of(predicate), pageable);
    }

    @GetMapping
    public R<List<ContactDTO>> list(@QuerydslPredicate(root = Contact.class) Predicate predicate,
        Pageable pageable) {
        QContact qContact = QContact.contact;
        predicate = ExpressionUtils.and(predicate, qContact.userId.eq(StpUtil.getLoginIdAsString()));
        return contactService.queryContactListByPage(PredicateWrapper.of(predicate), pageable);
    }

    /**
     * 关联出行人创建接口
     *
     * @param addDTO 创建信息
     */
    @PostMapping
    public R<String> create(@RequestBody ContactEditDTO addDTO) {
        addDTO.check();
        User currentRealUser = securityTools.getLoginUser(StpUtil.getLoginIdAsString());
        // 创建并返回dbId
        String dbId = contactService.createContact(addDTO, currentRealUser);
        return R.success("创建成功",dbId);
    }

    /**
     * 关联出行人更新接口
     *
     * @param editDto 更新信息
     */
    @PutMapping
    public R<Boolean> update(@RequestBody ContactEditDTO editDto) {
        // 基础校验
        if (CheckTools.isNullOrEmpty(editDto.getId())) {
            return R.failure("关联出行人id不存在" );
        }
        User currentRealUser = securityTools.getLoginUser(StpUtil.getLoginIdAsString());
        // 更新关联出行人
        contactService.updateContact(editDto, currentRealUser);
        return R.success("更新成功", true);
    }

    /**
     * 关联出行人删除接口
     *
     * @param id ID
     */
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable String id) {
        // 获取当前操作用户
        User currentRealUser = securityTools.getLoginUser("");
        // 调用service删除
        contactService.deleteContact(id,currentRealUser);
        return R.success("删除成功", true);
    }

    /**
     * 关联出行人批量删除接口
     *
     * @param ids id集合
     */
    @SaCheckRole("super-admin")
    @Transactional(rollbackFor = Exception.class)
    @DeleteMapping("/batch/{ids}")
    public R<Boolean> deleteBatch(@PathVariable String ids) {
        // 参数预处理
        if (StrUtil.isEmptyIfStr(ids)) return R.failure();

        // 获取当前操作用户
        User currentRealUser = securityTools.getLoginUser("");
        // 调用service删除
        List<String> idList = Arrays.stream(ids.split(",")).collect(Collectors.toList());
        contactService.deleteContactBatch(idList,currentRealUser);

        return R.success("批量删除成功", true);
    }

    /**
     * 根据ID查询关联出行人接口
     *
     * @param id ID
     */
    @GetMapping("/{id}")
    public R<ContactEditDTO> queryContactById(@PathVariable String id) {
        ContactEditDTO contactEditDTO = contactService.queryContactById(id);
        return R.success("查询成功",contactEditDTO);
    }

    /**
     * 关联出行人数据导出接口
     *
     * @param predicate 条件谓语
     * @param response  相应
     * @throws IOException IO异常
     */
    @GetMapping("/export")
    @SaCheckRole("admin")
    public void export(@QuerydslPredicate(root = Contact.class) Predicate predicate, HttpServletResponse response) throws IOException {
        String fileName = "关联出行人列表导出.xls";
        exportExcelBefore(response, fileName);
        EasyExcel.write(response.getOutputStream(), ContactDTO.class)
                .sheet("Sheet1")
                .doWrite(contactService.export(PredicateWrapper.of(predicate)));
    }


}
