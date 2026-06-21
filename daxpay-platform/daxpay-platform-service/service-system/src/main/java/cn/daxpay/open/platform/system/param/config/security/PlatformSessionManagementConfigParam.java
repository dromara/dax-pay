package cn.daxpay.open.platform.system.param.config.security;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 会话管理配置参数
///
@Data
@Accessors(chain = true)
@Schema(title = "会话管理配置参数")
public class PlatformSessionManagementConfigParam {

    @Schema(description = "是否启用会话管理")
    private Boolean enabled;

    @Schema(description = "最大在线时长（小时）")
    private Integer maxOnlineHours;

    @Schema(description = "最大并发会话数")
    private Integer maxConcurrentSessions;

    @Schema(description = "并发会话策略")
    private String concurrentStrategy;
}
