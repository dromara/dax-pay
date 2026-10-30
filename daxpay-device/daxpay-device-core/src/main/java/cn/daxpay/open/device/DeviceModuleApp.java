package cn.daxpay.open.device;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/// # 设备域自动配置
///
/// 设备域(虚拟设备: 收款播报 APP)自 `daxpay-payment-core` 拆出为独立一级模块,
/// 包根在支付域扫描树(`cn.daxpay.open.payment.**`)之外, 必须经
/// `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` 显式装配。
/// 支付核心经 [cn.daxpay.open.payment.strategy.device.PaySuccessDeviceHook] 等 SPI 反向调用本域,
/// 本模块单向依赖 `daxpay-payment-core`。
@AutoConfiguration
@MapperScan(annotationClass = Mapper.class)
@ComponentScan
public class DeviceModuleApp {
}
