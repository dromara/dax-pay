package cn.daxpay.open.device.support;

import cn.daxpay.open.device.virtual.dao.VirtualDeviceManager;
import cn.daxpay.open.device.virtual.entity.VirtualDevice;
import cn.daxpay.open.payment.strategy.device.MchDeviceCountSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/// # 商户维度设备域计数支持实现(虚拟设备)
///
/// 工作台「收银与设备」磁贴计数角标的设备域部分, 实现 [MchDeviceCountSupport]。
/// core 侧经 ObjectProvider 空判注入, 设备域缺席时优雅降级为零值。
/// 按 mch_no 相等计数(存量总数, 只过滤逻辑删除, 不按启用/激活状态过滤),
/// 相等条件天然排除平台库存/未绑定设备(mch_no 为空)。
@RequiredArgsConstructor
@Service
public class MchDeviceCountSupportImpl implements MchDeviceCountSupport {

    private final VirtualDeviceManager virtualDeviceManager;

    @Override
    public MchDeviceCount countByMchNo(String mchNo) {
        long virtualCount = virtualDeviceManager.lambdaQuery()
                .eq(VirtualDevice::getMchNo, mchNo)
                .count();
        return new MchDeviceCount(virtualCount);
    }
}
