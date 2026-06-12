package org.dromara.daxpay.platform.iam.service.role;

import org.dromara.daxpay.platform.common.mybatisplus.util.MpUtil;
import org.dromara.daxpay.platform.core.rest.dto.KeyValue;
import org.dromara.daxpay.platform.core.rest.param.PageParam;
import org.dromara.daxpay.platform.core.rest.result.PageResult;
import org.dromara.daxpay.platform.iam.dao.role.RoleManager;
import org.dromara.daxpay.platform.iam.entity.role.Role;
import org.dromara.daxpay.platform.iam.exception.role.RoleNotExistedException;
import org.dromara.daxpay.platform.iam.param.role.RoleQuery;
import org.dromara.daxpay.platform.iam.result.role.RoleResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/// # 角色查询
///
@Slf4j
@Service
@RequiredArgsConstructor
public class RoleQueryService {

    private final RoleManager roleManager;

    /// 角色列表
    public List<RoleResult> findAll() {
        return MpUtil.toListResult(roleManager.findAll());
    }

    /// 角色分页
    public PageResult<RoleResult> page(PageParam pageParam, RoleQuery query) {
        return MpUtil.toPageResult(roleManager.page(pageParam, query));
    }

    /// 角色下拉框
    public List<KeyValue> dropdown() {
        return roleManager.findDropdown();
    }

    /// 详情
    public RoleResult findById(Long id) {
        return roleManager.findById(id).map(Role::toResult).orElseThrow(RoleNotExistedException::new);
    }

    /// code是否存在
    public boolean existsByCode(String code) {
        return roleManager.existsByCode(code);
    }

    /// code是否存在
    public boolean existsByCode(String code, Long id) {
        return roleManager.existsByCode(code, id);
    }

    /// 中文名称是否存在
    public boolean existsByNameCn(String nameCn) {
        return roleManager.existsByNameCn(nameCn);
    }

    /// 中文名称是否存在(排除指定ID)
    public boolean existsByNameCn(String nameCn, Long id) {
        return roleManager.existsByNameCn(nameCn, id);
    }

    /// 英文名称是否存在
    public boolean existsByNameEn(String nameEn) {
        return roleManager.existsByNameEn(nameEn);
    }

    /// 英文名称是否存在(排除指定ID)
    public boolean existsByNameEn(String nameEn, Long id) {
        return roleManager.existsByNameEn(nameEn, id);
    }

}
