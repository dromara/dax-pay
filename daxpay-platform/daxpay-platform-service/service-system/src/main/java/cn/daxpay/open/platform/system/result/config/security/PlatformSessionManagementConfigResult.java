package cn.daxpay.open.platform.system.result.config.security;

import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 会话管理配置结果
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "会话管理配置结果")
public class PlatformSessionManagementConfigResult extends BaseResult {

    @Schema(description = "是否启用会话管理")
    private Boolean enabled;

    @Schema(description = "最大在线时长（小时）")
    private Integer maxOnlineHours;

    @Schema(description = "最大活跃时长（小时），0或留空表示不限制")
    private Integer activeTimeoutHours;

    @Schema(description = "最大并发会话数")
    private Integer maxConcurrentSessions;

    @Schema(description = "并发会话策略")
    private String concurrentStrategy;

    @Schema(description = "并发计数范围(GLOBAL全局共享 / PER_DEVICE按终端独立)")
    private String concurrentScope;
}
