package cn.daxpay.open.platform.iam.dao.dashboardpreference;

import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.iam.entity.dashboardpreference.UserDashboardPreference;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 用户工作台快捷入口偏好
///
@Repository
public class UserDashboardPreferenceManager extends BaseManager<UserDashboardPreferenceMapper, UserDashboardPreference> {

    /// 按用户 + 终端查询偏好
    public Optional<UserDashboardPreference> findByUserAndClient(Long userId, String clientCode) {
        return lambdaQuery()
                .eq(UserDashboardPreference::getUserId, userId)
                .eq(UserDashboardPreference::getClientCode, clientCode)
                .oneOpt();
    }

}
