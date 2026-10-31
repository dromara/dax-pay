package cn.daxpay.open.payment.strategy.device;

/// # 商户注销设备域钩子(SPI)
///
/// 商户删除(注销)链路对设备域的阻塞校验与级联清理扩展点, 模式对齐 [PaySuccessDeviceHook]:
/// 实现放在设备域模块 `daxpay-device`(虚拟设备台账/应用设备联动配置在设备域, payment 不能编译依赖设备域),
/// 无 Bean 时商户删除主链路跳过设备域校验与级联(视为未装配设备模块)。
///
/// **契约**: 实现须只读查询(阻塞校验)或按 `mchNo` 收敛的清理(级联), 不得反向调用 payment 域服务;
/// 未装配设备模块时商户删除不受影响, 设备域挂账数据(绑定商户的虚拟设备/应用联动配置行)由设备域自行处置。
public interface MerchantDeleteDeviceHook {

    /// 商户名下绑定设备台数(删除前置阻塞校验, >0 即拒删)
    long countBoundByMchNo(String mchNo);

    /// 商户注销级联: 逻辑删设备域商户私有配置(虚拟设备绑定/应用设备联动配置等)
    void deleteMerchantScopedByMchNo(String mchNo);
}
