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
/// 按用户 + 身份域终端(clientCode: admin/merchant/gateway) + 壳(terminal: web/app)三维分池存储工作台快捷入口序列,
/// PC Web 端与移动管理端 App 同用 admin 身份域但各存一份, 互不覆盖.
/// 仅存已选入口的 key 有序列表, 入口元信息(图标/标题/路由)由前端各端自行维护, 后端不感知.
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "iam_user_dashboard_preference", autoResultMap = true)
public class UserDashboardPreference extends MpBaseEntity implements ToResult<QuickEntryResult> {

    /// 用户ID
    private Long userId;

    /// 身份域身份域编码(admin / merchant / gateway), 非 WEB/MOBILE 壳
    private String clientCode;

    /// 请求终端(壳维度): web=PC Web 端 / app=移动管理端, 与 clientCode(身份域)正交
    private String terminal;

    /// 已选快捷入口有序序列(纯 key 数组), 如 ["merchant","notify","app"]
    @TableField(typeHandler = StringListTypeHandler.class)
    private List<String> entries;

    @Override
    public QuickEntryResult toResult() {
        return UserDashboardPreferenceConvert.CONVERT.toResult(this);
    }

    /// 构造新实例(新增用)
    public static UserDashboardPreference init(Long userId, String clientCode, String terminal, List<String> entries) {
        return new UserDashboardPreference()
                .setUserId(userId)
                .setClientCode(clientCode)
                .setTerminal(terminal)
                .setEntries(entries);
    }

}
