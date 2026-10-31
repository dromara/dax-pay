package cn.daxpay.open.device.support;

import cn.daxpay.open.device.linkage.dao.MchAppDeviceConfigManager;
import cn.daxpay.open.device.linkage.entity.MchAppDeviceConfig;
import cn.daxpay.open.device.virtual.dao.VirtualDeviceManager;
import cn.daxpay.open.device.virtual.entity.VirtualDevice;
import cn.daxpay.open.payment.strategy.device.MerchantDeleteDeviceHook;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/// # 商户注销设备域钩子实现
///
/// 商户删除(注销)链路的设备域阻塞校验与级联清理, 实现 [MerchantDeleteDeviceHook]。
/// core 侧经 ObjectProvider 空判注入, 设备域缺席时商户删除主链路优雅跳过设备域。
///
/// - 阻塞校验: 商户名下绑定虚拟设备台数(>0 拒删, 提示先解绑设备);
/// - 级联清理: 商户名下残余设备防御性解绑回平台库存(正常流程已被阻塞校验拦住,
///   此处兜底并发绑定场景, 设备档案为平台资产不删除);
///   应用设备联动配置 [MchAppDeviceConfig] 按 mchNo 逻辑删(应用已随商户级联删除, 避免孤儿行)。
@RequiredArgsConstructor
@Service
public class MerchantDeleteDeviceHookImpl implements MerchantDeleteDeviceHook {

    private final VirtualDeviceManager virtualDeviceManager;

    private final MchAppDeviceConfigManager mchAppDeviceConfigManager;

    @Override
    public long countBoundByMchNo(String mchNo) {
        return virtualDeviceManager.lambdaQuery()
                .eq(VirtualDevice::getMchNo, mchNo)
                .count();
    }

    @Override
    public void deleteMerchantScopedByMchNo(String mchNo) {
        // 兜底解绑商户名下残余设备(回平台库存, 清空应用/门店配置; 设备档案保留)
        List<Long> residualIds = virtualDeviceManager.lambdaQuery()
                .select(VirtualDevice::getId)
                .eq(VirtualDevice::getMchNo, mchNo)
                .list()
                .stream()
                .map(VirtualDevice::getId)
                .toList();
        if (!residualIds.isEmpty()) {
            virtualDeviceManager.unbindMerchant(residualIds);
        }
        // 应用设备联动配置按商户逻辑删(随商户/应用注销清理, 避免孤儿行)
        mchAppDeviceConfigManager.lambdaUpdate()
                .eq(MchAppDeviceConfig::getMchNo, mchNo)
                .remove();
    }
}
