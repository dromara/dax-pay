package cn.daxpay.open.platform.capability.social;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/// # 第三方社交登录能力模块自动配置
///
/// 仅负责通用 OAuth 协议层的组件扫描(justauth 包下的 Request/Factory).
/// 业务部分(配置管理、SocialEndpoint、绑定关系等)已迁移至 service-iam.
///
@Slf4j
@AutoConfiguration
@ComponentScan
public class SocialAutoConfiguration {
}
