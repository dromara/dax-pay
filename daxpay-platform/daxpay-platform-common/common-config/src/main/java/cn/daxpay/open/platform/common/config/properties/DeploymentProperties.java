package cn.daxpay.open.platform.common.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/// # 部署模式配置
///
/// 控制启动期 fail-fast 校验是否启用, 由 [cn.daxpay.open.platform.common.config.DeploymentModeEnforcer]
/// 在 `Environment` 就绪后读取并执行校验。
///
/// 推断规则(`DeploymentModeEnforcer.resolveMode`):
/// - 显式配置 `daxpay.platform.deployment.mode` 时以其值为准
/// - 未显式配置时: `spring.profiles.active` 含 `prod` → [Mode#PROD], 否则 → [Mode#DEV]
///
/// `PROD` 模式下会校验一系列"开发态功能"开关(沙箱/超管/Swagger/敏感 actuator 端点等),
/// 任一未关闭则拒绝启动; `DEV` 模式不做任何校验, 保持现有开发体验。
@Getter
@Setter
@ConfigurationProperties(prefix = "daxpay.platform.deployment")
public class DeploymentProperties {

    /// 部署模式, 不设 Java 默认值(由 `DeploymentModeEnforcer` 按 profile 推断)
    private Mode mode;

    /// # 部署模式
    ///
    /// - `DEV`: 开发模式, 不校验, 各功能开关自由配置
    /// - `PROD`: 生产模式, 启动期强制校验, 开发态功能未关闭则拒绝启动
    public enum Mode {
        /// 开发模式
        DEV,
        /// 生产模式
        PROD
    }
}
