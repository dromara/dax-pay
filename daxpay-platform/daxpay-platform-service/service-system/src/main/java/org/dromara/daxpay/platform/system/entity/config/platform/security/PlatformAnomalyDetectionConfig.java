package org.dromara.daxpay.platform.system.entity.config.platform.security;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 异常登录检测配置
///
@Data
@Accessors(chain = true)
public class PlatformAnomalyDetectionConfig {

    /// 是否启用异常登录检测
    private Boolean enabled;
    /// 是否检测异常IP
    private Boolean detectAnomalousIp;
    /// IP风险阈值（0-100）
    private Integer ipRiskThreshold;
    /// 是否检测异常登录时间
    private Boolean detectAnomalousTime;
    /// 登录时间偏离阈值（小时）
    private Integer timeDeviationThreshold;
    /// 是否检测异常设备
    private Boolean detectAnomalousDevice;
    /// 检测到异常登录时是否拦截
    private Boolean blockOnAnomaly;
}
