package cn.daxpay.open.platform.iam.service.dashboardpreference;

import cn.daxpay.open.platform.capability.auth.util.SecurityUtil;
import cn.daxpay.open.platform.iam.dao.dashboardpreference.UserDashboardPreferenceManager;
import cn.daxpay.open.platform.iam.entity.dashboardpreference.UserDashboardPreference;
import cn.daxpay.open.platform.iam.result.dashboardpreference.QuickEntryResult;
import cn.daxpay.open.platform.iam.service.client.ClientCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/// # 用户工作台快捷入口偏好
///
/// PC 与移动端通过请求终端(clientCode)区分, 每用户每端一份配置, 互不影响.
/// 仅存储用户已选入口的 key 有序序列, 入口元信息由前端维护.
@Service
@RequiredArgsConstructor
public class UserDashboardPreferenceService {

    private final UserDashboardPreferenceManager userDashboardPreferenceManager;

    private final ClientCodeService clientCodeService;

    /// 查询当前用户在当前终端的快捷入口序列
    /// 未自定义时 entries 返回 null, 前端据此使用默认序列
    public QuickEntryResult findCurrent() {
        Long userId = SecurityUtil.getUserId();
        String clientCode = clientCodeService.getClientCode();
        return userDashboardPreferenceManager.findByUserAndClient(userId, clientCode)
                .map(UserDashboardPreference::toResult)
                .orElseGet(() -> new QuickEntryResult().setEntries(null));
    }

    /// 保存(整体覆盖)当前用户在当前终端的快捷入口序列
    @Transactional(rollbackFor = Exception.class)
    public void saveCurrent(List<String> entries) {
        Long userId = SecurityUtil.getUserId();
        String clientCode = clientCodeService.getClientCode();
        // 入参为 null 时按空序列处理, 表示全部隐藏
        List<String> safeEntries = entries == null ? new ArrayList<>() : entries;
        UserDashboardPreference existed = userDashboardPreferenceManager
                .findByUserAndClient(userId, clientCode).orElse(null);
        if (existed == null) {
            // 新增
            userDashboardPreferenceManager.save(
                    UserDashboardPreference.init(userId, clientCode, safeEntries));
        }
        else {
            // 整体覆盖更新
            existed.setEntries(safeEntries);
            userDashboardPreferenceManager.updateById(existed);
        }
    }

}
