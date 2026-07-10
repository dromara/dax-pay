package cn.daxpay.open.platform.capability.douyin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/// # 抖音开放平台能力模块自动配置
///
/// 装配抖音 H5 静默授权服务([DouyinH5AuthService], HTTP 直连无官方 SDK),
/// 供 payment 模块(平台级调试认证)等调用方使用, 不耦合配置存储。
///
@Slf4j
@AutoConfiguration
@ComponentScan
public class DouyinAutoConfiguration {
}
