package cn.daxpay.open.payment.admin.service.develop;

import cn.daxpay.open.payment.admin.param.develop.DevelopParam;
import cn.daxpay.open.payment.admin.result.develop.DevelopSignResult;
import cn.daxpay.open.payment.common.util.ObjectSignStrUtil;
import cn.daxpay.open.payment.common.util.PaySignUtil;
import cn.daxpay.open.payment.masterdata.result.provider.PayProviderMethodResult;
import cn.daxpay.open.payment.masterdata.service.provider.PayProviderMethodService;
import cn.daxpay.open.payment.route.service.support.PayRouteStrategyCapabilitySupport;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.platform.core.rest.dto.LabelValue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/// # 交易开发调试服务
///
/// 仅提供组参辅助与签名能力, **不发起真实交易**。
/// 真实支付由管理端调试页模拟商户 HTTP 请求调用 `/unipay/pay` 完成,
/// 与正式对接走同一入口, 避免 admin 内部直调支付核心形成后门。
@Slf4j
@Service
@RequiredArgsConstructor
public class DevelopTradeService {

    private final PayProviderMethodService payProviderMethodService;
    private final PayRouteStrategyCapabilitySupport payRouteStrategyCapabilitySupport;

    /// 生成支付参数签名(与正式签名逻辑一致)
    ///
    /// 调用方需在 param 中自备 reqTime / nonceStr 等公共字段, 本方法只负责签名串与签名值。
    public DevelopSignResult sign(DevelopParam<NormalPayParam> param) {
        // 签名串(与 PaySignUtil 内部一致)
        String signStr = ObjectSignStrUtil.buildSignStr(param.getParam());
        // 签名值
        String sign = PaySignUtil.sign(param.getParam(), param.getPrivateKey());
        return new DevelopSignResult().setSignStr(signStr).setSign(sign);
    }

    /// 已启用渠道支付方式目录（供调试页支付方式下拉）
    public List<PayProviderMethodResult> listMethodDirectory() {
        return payProviderMethodService.listDirectoryFlat();
    }

    /// 直接指定：商户全部启用通道商户候选, provider 为空返回全部,
    /// 非空按支付渠道过滤(如 wechat 返回所有声明支持微信的产品通道商户, 含官方与三方聚合通道)
    public List<LabelValue> listChannelMchCandidates(String mchNo, String provider) {
        return payRouteStrategyCapabilitySupport.listDirectChannelMchCandidates(mchNo, provider);
    }

    /// 直接指定：按通道商户(产品)返回全部启用支付能力候选
    public List<LabelValue> listCapabilityCandidates(String channelMchNo) {
        return payRouteStrategyCapabilitySupport.listDirectCapabilityCandidates(channelMchNo);
    }
}
