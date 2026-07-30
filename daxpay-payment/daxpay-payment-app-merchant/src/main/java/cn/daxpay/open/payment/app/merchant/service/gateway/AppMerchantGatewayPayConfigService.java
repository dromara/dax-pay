package cn.daxpay.open.payment.app.merchant.service.gateway;

import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.merchant.param.gateway.GatewayPayConfigParam;
import cn.daxpay.open.payment.merchant.result.appinfo.MchAppInfoResult;
import cn.daxpay.open.payment.merchant.result.gateway.GatewayPayConfigResult;
import cn.daxpay.open.payment.merchant.service.appinfo.MchAppInfoService;
import cn.daxpay.open.payment.merchant.service.gateway.GatewayPayConfigService;
import cn.daxpay.open.payment.route.service.support.PayRouteStrategyCapabilitySupport;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.rest.dto.ChannelMchOption;
import cn.daxpay.open.platform.core.rest.dto.LabelValue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/// # 商户移动端-网关支付配置服务(码牌/聚合共用)
///
/// 转发至 core [GatewayPayConfigService]；写操作强制当前上下文 mchNo。
@Service
@RequiredArgsConstructor
public class AppMerchantGatewayPayConfigService {

    private final GatewayPayConfigService gatewayPayConfigService;
    private final PayRouteStrategyCapabilitySupport payRouteStrategyCapabilitySupport;
    private final MchAppInfoService mchAppInfoService;
    private final PaymentContext paymentContext;

    /// 当前登录商户号
    private String requireMchNo() {
        String mchNo = paymentContext.getMchNo();
        if (mchNo == null || mchNo.isBlank()) {
            // 商户上下文缺失
            throw new BizInfoException(CommonCode.FAIL_CODE, "pay.error.assist.mchContextMissing");
        }
        return mchNo;
    }

    /// 按应用查询网关支付配置
    public GatewayPayConfigResult findByAppId(String appId) {
        // 校验 appId 属于当前商户
        mchAppInfoService.findByAppId(appId);
        return gatewayPayConfigService.findByAppId(appId);
    }

    /// 保存或更新网关支付配置
    public void saveOrUpdate(GatewayPayConfigParam param) {
        MchAppInfoResult app = mchAppInfoService.findByAppId(param.getAppId());
        // 强制当前商户号，忽略客户端传入
        param.setMchNo(app.getMchNo() != null ? app.getMchNo() : requireMchNo());
        gatewayPayConfigService.saveOrUpdate(param);
    }

    /// DIRECT 模式: 通道商户候选
    public List<ChannelMchOption> listDirectChannelMchCandidates(String provider) {
        return payRouteStrategyCapabilitySupport.listDirectChannelMchCandidates(requireMchNo(), provider);
    }

    /// DIRECT 模式: 支付能力候选
    public List<LabelValue> listDirectCapabilityCandidates(String channelMchNo) {
        return payRouteStrategyCapabilitySupport.listDirectCapabilityCandidates(channelMchNo);
    }
}
