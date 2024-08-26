package cn.bootx.platform.iam.dao.user;

import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.iam.entity.user.UserInfo;
import cn.bootx.platform.iam.param.user.UserInfoQuery;
import cn.bootx.platform.starter.auth.util.SecurityUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 用户信息
 *
 * @author xxm
 * @since 2020/4/24 15:32
 */
@Repository
@RequiredArgsConstructor
public class UserInfoManager extends BaseManager<UserInfoMapper, UserInfo> {

    public boolean existsByAccount(String account) {
        return existedByField(UserInfo::getAccount, account);
    }

    public boolean existsByAccount(String account, Long id) {
        return existedByField(UserInfo::getAccount, account, id);
    }

    public boolean existsByEmail(String email) {
        return existedByField(UserInfo::getEmail, email);
    }

    public boolean existsByEmail(String email, Long id) {
        return existedByField(UserInfo::getEmail, email, id);
    }

    public boolean existsByPhone(String phone) {
        return existedByField(UserInfo::getPhone, phone);
    }

    public boolean existsByPhone(String phone, Long id) {
        return existedByField(UserInfo::getPhone, phone, id);
    }

    public Optional<UserInfo> findByAccount(String account) {
        return findByField(UserInfo::getAccount, account);
    }

    public Optional<UserInfo> findByEmail(String email) {
        return findByField(UserInfo::getEmail, email);
    }

    public Optional<UserInfo> findByPhone(String phone) {
        return findByField(UserInfo::getPhone, phone);
    }

    /**
     * 管理员用户不显示
     */
    public Page<UserInfo> page(PageParam pageParam, UserInfoQuery query) {
        Page<UserInfo> mpPage = MpUtil.getMpPage(pageParam);
        QueryWrapper<UserInfo> generator = QueryGenerator.generator(query);
        generator.eq(MpUtil.getColumnName(UserInfo::isAdministrator), false);
        return this.page(mpPage, generator);
    }

    public void setUpStatus(Long userId, String status) {
        lambdaUpdate().eq(MpIdEntity::getId, userId).set(UserInfo::getStatus, status).update();
    }

    /**
     * 批量更新用户状态
     */
    public void setUpStatusBatch(List<Long> userIds, String status) {
        lambdaUpdate()
                .in(MpIdEntity::getId, userIds)
                .set(UserInfo::getStatus, status)
                .set(UserInfo::getLastModifiedTime, LocalDateTime.now())
                .set(UserInfo::getLastModifier, SecurityUtil.getUserIdOrDefaultId())
                .update();
    }

    /**
     * 批量重置用户密码
     */
    public void restartPasswordBatch(List<Long> userIds,String password){
        lambdaUpdate()
                .in(MpIdEntity::getId, userIds)
                .set(UserInfo::getPassword, password)
                .set(UserInfo::getLastModifiedTime, LocalDateTime.now())
                .set(UserInfo::getLastModifier, SecurityUtil.getUserIdOrDefaultId())
                .update();
    }

}
