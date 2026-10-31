package cn.daxpay.open.payment.strategy.device;

/// # 商户维度设备域计数支持(可短路 SPI)
///
/// 实现放在设备域模块 `daxpay-device`, 服务于工作台「收银与设备」磁贴数字角标:
/// 虚拟设备台账在设备域, 运营端/商户端模块不依赖 device-core, 经本 SPI 反向取数。
/// 无 Bean 时按零处理(计数为零), 不影响工作台主链路。
///
/// **范围**: 微信/抖音商户应用属支付链路域(留 core), 各端直查, 不在此接口内。
public interface MchDeviceCountSupport {

    /// 商户名下的虚拟设备存量计数(mch_no 匹配, 逻辑删除自动过滤;
    /// 只数商户名下设备: 平台库存/未绑定设备(mch_no 为空)天然排除)
    MchDeviceCount countByMchNo(String mchNo);

    /// 商户维度设备域计数快照
    record MchDeviceCount(long virtualDeviceCount) {

        /// 无设备域模块时的零值快照
        public static MchDeviceCount empty() {
            return new MchDeviceCount(0);
        }
    }
}
