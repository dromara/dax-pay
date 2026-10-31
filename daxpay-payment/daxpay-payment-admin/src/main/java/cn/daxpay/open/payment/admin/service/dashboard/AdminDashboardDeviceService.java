package cn.daxpay.open.payment.admin.service.dashboard;

import cn.daxpay.open.payment.admin.result.dashboard.AdminMchDeviceCountResult;
import cn.daxpay.open.payment.douyin.dao.merchant.DyMchAppManager;
import cn.daxpay.open.payment.strategy.device.MchDeviceCountSupport;
import cn.daxpay.open.payment.wx.dao.merchant.WxMchAppManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

/// # 商户设备与应用计数服务(运营端)
///
/// 与商户端同名服务同构, 差异仅在商户号来源: 运营端显式指定 mchNo(跨商户查看),
/// 商户端强制取会话上下文防越权。虚拟设备实体在设备域, 经
/// [MchDeviceCountSupport] SPI 取数(设备域缺席时计数为零);
/// 微信/抖音商户应用在 core, 直查计数。
@Service
@RequiredArgsConstructor
public class AdminDashboardDeviceService {

    private final ObjectProvider<MchDeviceCountSupport> mchDeviceCountSupportProvider;
    private final WxMchAppManager wxMchAppManager;
    private final DyMchAppManager dyMchAppManager;

    /// 指定商户的设备与支付应用计数(虚拟设备/微信应用/抖音应用)
    ///
    /// 计数口径: 一律存量总数, 只过滤逻辑删除, 不按启用/状态过滤。
    public AdminMchDeviceCountResult counts(String mchNo) {
        AdminMchDeviceCountResult result = new AdminMchDeviceCountResult();
        // 虚拟设备数(设备域 SPI, 缺席时零值降级)
        MchDeviceCountSupport countSupport = mchDeviceCountSupportProvider.getIfAvailable();
        MchDeviceCountSupport.MchDeviceCount deviceCount = countSupport == null
                ? MchDeviceCountSupport.MchDeviceCount.empty()
                : countSupport.countByMchNo(mchNo);
        result.setVirtualDeviceCount((int) deviceCount.virtualDeviceCount());
        // 商户微信/抖音开放应用数
        result.setWxAppCount((int) wxMchAppManager.countByMchNo(mchNo));
        result.setDyAppCount((int) dyMchAppManager.countByMchNo(mchNo));
        return result;
    }
}
