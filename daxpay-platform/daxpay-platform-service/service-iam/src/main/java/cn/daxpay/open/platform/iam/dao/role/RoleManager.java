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

    /// 根据中文名称判断是否存在
    public boolean existsByNameCn(String nameCn) {
        return existedByField(Role::getNameCn, nameCn);
    }

    /// 根据中文名称判断是否存在(排除指定ID)
    public boolean existsByNameCn(String nameCn, Long id) {
        return existedByField(Role::getNameCn, nameCn, id);
    }

    /// 根据英文名称判断是否存在
    public boolean existsByNameEn(String nameEn) {
        return existedByField(Role::getNameEn, nameEn);
    }

    /// 根据英文名称判断是否存在(排除指定ID)
    public boolean existsByNameEn(String nameEn, Long id) {
        return existedByField(Role::getNameEn, nameEn, id);
    }

    /// 根据当前语言判断名称是否存在
    public boolean existsByName(String nameCn, String nameEn) {
        return existedByField(Role::getNameCn, nameCn);
    }

    /// 根据当前语言判断名称是否存在(排除指定ID)
    public boolean existsByName(String nameCn, String nameEn, Long id) {
        return existedByField(Role::getNameCn, nameCn, id);
    }

    /// 下拉框查询，根据语言返回对应名称
    public List<KeyValue> findDropdown() {
        return lambdaQuery().select(Role::getId, Role::getNameCn, Role::getNameEn)
            .list()
            .stream()
            .map(role -> new KeyValue(String.valueOf(role.getId()),role.getNameCn()))
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

