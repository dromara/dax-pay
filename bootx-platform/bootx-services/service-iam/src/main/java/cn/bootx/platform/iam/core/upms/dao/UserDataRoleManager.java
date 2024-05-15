package cn.bootx.platform.iam.core.upms.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.iam.core.upms.entity.UserDataRole;
import cn.bootx.platform.starter.auth.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author xxm
 * @since 2021/12/23
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class UserDataRoleManager extends BaseManager<UserDataRoleMapper, UserDataRole> {

    public boolean existsByDataRoleId(Long dataRoleId) {
        return this.existedByField(UserDataRole::getRoleId, dataRoleId);
    }

    public void deleteByUser(Long userId) {
        this.deleteByField(UserDataRole::getUserId, userId);
    }

    public void deleteByUsers(List<Long> userIds) {
        this.deleteByFields(UserDataRole::getUserId, userIds);
    }

    public List<UserDataRole> findAllByUserId(Long userId) {
        return this.findAllByField(UserDataRole::getUserId, userId);
    }

    public Optional<UserDataRole> findByUserId(Long userId) {
        return this.findByField(UserDataRole::getUserId, userId);
    }

    @Override
    public List<UserDataRole> saveAll(List<UserDataRole> dataScopes) {
        MpUtil.initEntityList(dataScopes, SecurityUtil.getUserIdOrDefaultId());
        MpUtil.executeBatch(dataScopes, baseMapper::saveAll, this.DEFAULT_BATCH_SIZE);
        return dataScopes;
    }

}
