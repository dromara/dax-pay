package cn.daxpay.open.platform.system.entity.config.platform.infra;

import cn.daxpay.open.platform.system.enums.PlatformConfigTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 平台敏感词策略配置
///
/// 总开关、是否回显命中词、是否写审计等.
/// 全局唯一, 通过 [PlatformConfigTypeEnum#SENSITIVE_WORD] 以 JSON 存于 `system_platform_config`.
///
@Data
@Accessors(chain = true)
public class PlatformSensitiveWordConfig {

    /// 是否启用敏感词过滤（默认 true；海外可关）
    private Boolean enabled = true;

    /// 对外错误是否回显命中词（默认 false，防探测）
    private Boolean revealWord = false;

    /// 是否写入命中审计表（默认 true）
    private Boolean recordHit = true;

    /// 命中原文摘要最大长度
    private Integer contentPreviewMaxLen = 200;
}
