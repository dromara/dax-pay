package cn.daxpay.open.channel.mock;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

/// # Mock 模拟通道编排层
///
/// 对接独立 mock 子应用(扮演"三方支付平台")的通道编排模块, 形态与支付宝编排模块
/// [cn.daxpay.open.channel.alipay.AlipayChannelApp] 同构:
/// 经 [cn.daxpay.open.channel.mock.client.MockChannelClient] 转发执行, 传输层 AES-GCM 加密由平台统一挂载。
///
/// ## 生产防线
/// 挂 `daxpay.platform.deployment.mode` 条件装配: 仅 DEV 装配,
/// PROD 下本模块全部 beans 静默不注册, 误配 mock 通道商户时路由直接报"产品策略不存在",
/// 天然 fail-fast; 逃生口 `DAXPAY_PLATFORM_DEPLOYMENT_MODE=DEV` 与既有校验语义一致。
/// 本模块含通道私有表(mock_pay_credential / mock_gateway_credential)的 Mapper,
/// 经 @MapperScan 按 @Mapper 注解扫描注册。
@AutoConfiguration
@ConditionalOnProperty(name = "daxpay.platform.deployment.mode", havingValue = "DEV", matchIfMissing = true)
@ConfigurationPropertiesScan
@MapperScan(annotationClass = Mapper.class)
@ComponentScan
public class MockChannelApp {
}
