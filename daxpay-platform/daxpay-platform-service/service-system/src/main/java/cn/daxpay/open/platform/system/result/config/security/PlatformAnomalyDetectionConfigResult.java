package cn.daxpay.open.platform.system.result.config.security;

import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 异常登录检测配置结果
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "异常登录检测配置结果")
public class PlatformAnomalyDetectionConfigResult extends BaseResult {

    @Schema(description = "是否启用异常登录检测")
    private Boolean enabled;

    @Schema(description = "是否检测异常IP")
    private Boolean detectAnomalousIp;

    @Schema(description = "IP风险阈值（0-100）")
    private Integer ipRiskThreshold;

    @Schema(description = "是否检测异常登录时间")
    private Boolean detectAnomalousTime;

    @Schema(description = "登录时间偏离阈值（小时）")
    private Integer timeDeviationThreshold;

    @Schema(description = "是否检测异常设备")
    private Boolean detectAnomalousDevice;

    @Schema(description = "检测到异常登录时是否拦截")
    private Boolean blockOnAnomaly;
}
