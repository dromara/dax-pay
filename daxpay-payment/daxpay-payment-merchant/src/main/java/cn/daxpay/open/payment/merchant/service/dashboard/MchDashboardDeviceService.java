package cn.daxpay.open.payment.merchant.service.dashboard;

import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.douyin.dao.merchant.DyMchAppManager;
import cn.daxpay.open.payment.merchant.result.dashboard.MchDeviceCountResult;
import cn.daxpay.open.payment.strategy.device.MchDeviceCountSupport;
import cn.daxpay.open.payment.wx.dao.merchant.WxMchAppManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 商户端工作台设备与应用计数
///
/// 聚合应用工作台「通知与设备」等卡片计数: 虚拟设备经 [MchDeviceCountSupport] SPI
/// (设备域缺席时计数为零), 微信/抖音商户应用在 core 直查。
/// 强制使用 [PaymentContext#getMchNo], 禁止请求参数指定任意商户号。
@Service
@RequiredArgsConstructor
public class MchDashboardDeviceService {

    private final PaymentContext paymentContext;
    private final ObjectProvider<MchDeviceCountSupport> mchDeviceCountSupportProvider;
    private final WxMchAppManager wxMchAppManager;
    private final DyMchAppManager dyMchAppManager;

    /// 解析并校验当前商户号
    private String requireMchNo() {
        String mchNo = paymentContext.getMchNo();
        if (Objects.isNull(mchNo) || mchNo.isBlank()) {
            // 商户: 数据错误未发现商户号
            throw new BizInfoException(CommonCode.FAIL_CODE, "error.payment.merchant.dataErrorNoMchNo");
        }
        return mchNo;
    }

    /// 工作台设备与应用: 三类计数(虚拟设备/微信应用/抖音应用)
    ///
    /// 计数口径: 一律存量总数, 只过滤逻辑删除, 不按启用/状态过滤。
    public MchDeviceCountResult deviceCounts() {
        String mchNo = requireMchNo();
        MchDeviceCountResult result = new MchDeviceCountResult();
        // 名下虚拟设备数(设备域 SPI, 缺席时零值降级)
        MchDeviceCountSupport countSupport = mchDeviceCountSupportProvider.getIfAvailable();
        MchDeviceCountSupport.MchDeviceCount deviceCount = countSupport == null
                ? MchDeviceCountSupport.MchDeviceCount.empty()
                : countSupport.countByMchNo(mchNo);
        result.setVirtualDeviceCount((int) deviceCount.virtualDeviceCount());
        // 商户微信/抖音开放应用数(按登录商户隔离)
        result.setWxAppCount((int) wxMchAppManager.countByMchNo(mchNo));
        result.setDyAppCount((int) dyMchAppManager.countByMchNo(mchNo));
        return result;
    }
}
