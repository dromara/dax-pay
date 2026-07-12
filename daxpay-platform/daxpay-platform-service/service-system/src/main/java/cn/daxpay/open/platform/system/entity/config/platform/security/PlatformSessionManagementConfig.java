package cn.daxpay.open.platform.system.entity.config.platform.security;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 会话管理配置
///
@Data
@Accessors(chain = true)
public class PlatformSessionManagementConfig {

    /// 是否启用会话管理
    private Boolean enabled;
    /// 最大在线时长（小时）
    private Integer maxOnlineHours;
    /// 最大活跃时长(小时), 0或null表示不限制
    private Integer activeTimeoutHours;
    /// 最大并发会话数
    private Integer maxConcurrentSessions;
    /// 并发会话策略
    private String concurrentStrategy;
}
