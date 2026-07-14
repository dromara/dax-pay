package cn.daxpay.open.platform.iam.service.role;

import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.rest.dto.KeyValue;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.iam.dao.role.RoleManager;
import cn.daxpay.open.platform.iam.entity.role.Role;
import cn.daxpay.open.platform.iam.exception.role.RoleNotExistedException;
import cn.daxpay.open.platform.iam.param.role.RoleQuery;
import cn.daxpay.open.platform.iam.result.role.RoleResult;
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

}
