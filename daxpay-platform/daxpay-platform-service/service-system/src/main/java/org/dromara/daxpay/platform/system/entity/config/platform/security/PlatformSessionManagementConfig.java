package org.dromara.daxpay.platform.system.entity.config.platform.security;

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
    /// 最大并发会话数
    private Integer maxConcurrentSessions;
    /// 并发会话策略
    private String concurrentStrategy;
}
