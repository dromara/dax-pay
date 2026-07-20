package cn.daxpay.open.payment.check.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/// # 配置告警严重程度
///
/// 当前阶段统一为建议级(SUGGEST), 预留扩展位用于后续划分阻塞级。
@Getter
@RequiredArgsConstructor
public enum ConfigCheckSeverityEnum {

    /// 建议级: 影响功能完整性, 不配不会立即阻断核心流程
    SUGGEST("suggest"),
    /// 阻塞级: 不配置将导致核心能力不可用(预留)
    BLOCKER("blocker");

    private final String code;
}
