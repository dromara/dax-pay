package cn.daxpay.open.platform.iam.dao.role;

import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.rest.dto.KeyValue;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.iam.entity.role.Role;
import cn.daxpay.open.platform.iam.param.role.RoleQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/// # 角色
///
@Slf4j
@Repository
@RequiredArgsConstructor
public class RoleManager extends BaseManager<RoleMapper, Role> {

    public boolean existsByCode(String code) {
        return existedByField(Role::getCode, code);
    }

    public boolean existsByCode(String code, Long id) {
        return existedByField(Role::getCode, code, id);
    }

    /// 下拉框查询，返回 i18nKey 供前端翻译
    public List<KeyValue> findDropdown() {
        return lambdaQuery().select(Role::getId, Role::getCode, Role::getI18nKey)
            .list()
            .stream()
            .map(role -> new KeyValue(String.valueOf(role.getId()),role.getI18nKey()))
            .collect(Collectors.toList());
    }

    /// 按终端查询角色列表
    public List<Role> findAllByClientCode(String clientCode) {
        return lambdaQuery()
                .eq(Role::getClientCode, clientCode)
                .list();
    }

    public Optional<Role> findByCode(String code) {
        return findByField(Role::getCode, code);
    }

    public Page<Role> page(PageParam pageParam, RoleQuery query) {
        Page<Role> mpPage = MpUtil.getMpPage(pageParam);
        QueryWrapper<Role> generator = QueryGenerator.generator(query);
        return this.page(mpPage, generator);
    }
}

