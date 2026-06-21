package cn.daxpay.open.platform.system.param.config.security;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 异常登录检测配置参数
///
@Data
@Accessors(chain = true)
@Schema(title = "异常登录检测配置参数")
public class PlatformAnomalyDetectionConfigParam {

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
