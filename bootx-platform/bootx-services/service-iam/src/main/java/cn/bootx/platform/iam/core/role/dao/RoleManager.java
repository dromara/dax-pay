package cn.bootx.platform.iam.core.role.dao;

import cn.bootx.platform.common.core.rest.dto.KeyValue;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.iam.core.role.entity.Role;
import cn.bootx.platform.iam.param.role.RoleParam;
import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色
 *
 * @author xxm
 * @since 2021/8/3
 */
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

    public boolean existsByName(String name) {
        return existedByField(Role::getName, name);
    }

    public boolean existsByName(String name, Long id) {
        return existedByField(Role::getName, name, id);
    }

    public boolean existsById(Long roleId) {
        return lambdaQuery().eq(Role::getId, roleId).exists();
    }

    public List<KeyValue> findDropdown() {
        return lambdaQuery().select(Role::getId, Role::getName)
            .list()
            .stream()
            .map(role -> new KeyValue(String.valueOf(role.getId()), role.getName()))
            .collect(Collectors.toList());

    }

    public Page<Role> page(PageParam pageParam, RoleParam roleParam) {
        Page<Role> mpPage = MpUtil.getMpPage(pageParam, Role.class);
        return lambdaQuery().like(StrUtil.isNotBlank(roleParam.getCode()), Role::getCode, roleParam.getCode())
            .like(StrUtil.isNotBlank(roleParam.getName()), Role::getName, roleParam.getName())
            .orderByDesc(MpIdEntity::getId)
            .page(mpPage);
    }

    public boolean existsByPid(Long roleId) {
        return lambdaQuery().eq(Role::getPid, roleId).exists();
    }
}
