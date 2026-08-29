package cn.daxpay.open.platform.iam.dao.dashboardpreference;

import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.iam.entity.dashboardpreference.UserDashboardPreference;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 用户工作台快捷入口偏好
///
@Repository
public class UserDashboardPreferenceManager extends BaseManager<UserDashboardPreferenceMapper, UserDashboardPreference> {

    /// 按用户 + 身份域终端 + 壳(web/app)查询偏好
    public Optional<UserDashboardPreference> findByUserAndClientAndTerminal(Long userId, String clientCode, String terminal) {
        return lambdaQuery()
                .eq(UserDashboardPreference::getUserId, userId)
                .eq(UserDashboardPreference::getClientCode, clientCode)
                .eq(UserDashboardPreference::getTerminal, terminal)
                .oneOpt();
    }

}
