package cn.daxpay.open.platform.iam.entity.dashboardpreference;

import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.common.mybatisplus.handler.type.StringListTypeHandler;
import cn.daxpay.open.platform.iam.convert.dashboardpreference.UserDashboardPreferenceConvert;
import cn.daxpay.open.platform.iam.result.dashboardpreference.QuickEntryResult;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/// # 用户工作台快捷入口偏好
///
/// 按用户 + 终端(clientCode)维度存储用户在工作台自定义的快捷入口序列.
/// 仅存已选入口的 key 有序列表, 入口元信息(图标/标题/路由)由前端各端自行维护, 后端不感知.
/// PC 与移动端通过 clientCode 区分, 互不影响.
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "iam_user_dashboard_preference", autoResultMap = true)
public class UserDashboardPreference extends MpBaseEntity implements ToResult<QuickEntryResult> {

    /// 用户ID
    private Long userId;

    /// 终端编码(WEB / MOBILE), PC 与移动分开管理
    private String clientCode;

    /// 已选快捷入口有序序列(纯 key 数组), 如 ["merchant","notify","app"]
    @TableField(typeHandler = StringListTypeHandler.class)
    private List<String> entries;

    @Override
    public QuickEntryResult toResult() {
        return UserDashboardPreferenceConvert.CONVERT.toResult(this);
    }

    /// 构造新实例(新增用)
    public static UserDashboardPreference init(Long userId, String clientCode, List<String> entries) {
        return new UserDashboardPreference()
                .setUserId(userId)
                .setClientCode(clientCode)
                .setEntries(entries);
    }

}
