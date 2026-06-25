package cn.daxpay.open.platform.iam.dao.twofactor;

import java.util.Optional;

import cn.daxpay.open.platform.iam.entity.twofactor.UserTwoFactor;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

/// # 用户双因素认证绑定 Manager
///
/// 封装绑定记录的数据访问, 按用户ID查询集中在此层, Service 层不直接使用 lambdaQuery。
///
@Repository
public class UserTwoFactorManager extends BaseManager<UserTwoFactorMapper, UserTwoFactor> {

    /// 根据用户ID查询绑定记录
    public Optional<UserTwoFactor> findByUserId(Long userId) {
        return lambdaQuery()
            .eq(UserTwoFactor::getUserId, userId)
            .oneOpt();
    }
}
