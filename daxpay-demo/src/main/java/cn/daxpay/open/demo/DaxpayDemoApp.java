package cn.daxpay.open.demo;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/// # 演示模块自动装配
///
/// 通过 Spring Boot 自动配置机制装配演示模块的 Controller / Consumer / Service。
/// `@ComponentScan` 默认扫描本包及其子包（含 artemis 各子包）。
@AutoConfiguration
@ComponentScan
public class DaxpayDemoApp {
}
