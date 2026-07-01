package cn.daxpay.open.payment.admin.service.develop;

import cn.daxpay.open.payment.admin.param.develop.DevelopParam;
import cn.daxpay.open.payment.admin.result.develop.DevelopPayResult;
import cn.daxpay.open.payment.admin.result.develop.DevelopSignResult;
import cn.daxpay.open.payment.common.context.PaymentAssistService;
import cn.daxpay.open.payment.common.context.PaymentContextManager;
import cn.daxpay.open.payment.common.util.ObjectSignStrUtil;
import cn.daxpay.open.payment.common.util.PaySignUtil;
import cn.daxpay.open.payment.masterdata.constants.provider.result.PayProviderMethodResult;
import cn.daxpay.open.payment.masterdata.constants.provider.service.PayProviderMethodService;
import cn.daxpay.open.payment.merchant.service.route.support.PayRouteStrategyCapabilitySupport;
import cn.daxpay.open.payment.pay.service.NormalPayService;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPayResult;
import cn.daxpay.open.platform.common.config.properties.PlatformConfigProperties;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.rest.dto.LabelValue;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/// # 交易开发调试服务
///
/// 复用普通支付核心服务 NormalPayService, 在管理端以指定商户/应用身份真实发起一笔支付,
/// 便于联调验证通道配置与签名是否正确
@Slf4j
@Service
@RequiredArgsConstructor
public class DevelopTradeService {

    private final NormalPayService normalPayService;
    private final PaymentContextManager paymentContextManager;
    private final PaymentAssistService paymentAssistService;
    private final PlatformConfigProperties platformConfigProperties;
    private final PayProviderMethodService payProviderMethodService;
    private final PayRouteStrategyCapabilitySupport payRouteStrategyCapabilitySupport;

    /// 生成支付参数签名(与正式签名逻辑一致)
    public DevelopSignResult sign(DevelopParam<NormalPayParam> param) {
        // 签名串(与 PaySignUtil 内部一致)
        String signStr = ObjectSignStrUtil.buildSignStr(param.getParam());
        // 签名值
        String sign = PaySignUtil.sign(param.getParam(), param.getPrivateKey());
        return new DevelopSignResult().setSignStr(signStr).setSign(sign);
    }

    /// 支付调试(真实发起)
    ///
    /// 安全流程与正式支付一致:
    /// 1. 使用传入的商户私钥对参数签名, 再用系统配置的商户公钥验签, 校验私钥是否正确
    /// 2. 发起真实支付
    /// 3. 使用平台私钥对支付结果签名, 便于客户端验证响应完整性
    public DevelopPayResult pay(DevelopParam<NormalPayParam> param) {
        NormalPayParam payParam = param.getParam();
        // 私钥必填
        if (StrUtil.isBlank(param.getPrivateKey())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.assist.privateKeyEmpty");
        }
        // 在支付作用域内执行, 初始化商户上下文, 使 mchNo/appId 等字段自动填充
        return paymentContextManager.executeWithScope(
                payParam.getMchNo(), payParam.getAppId(),
                () -> {
                    // 私钥验证: 用传入私钥签名, 再用系统配置的商户公钥验签
                    String sign = PaySignUtil.sign(payParam, param.getPrivateKey());
                    payParam.setSign(sign);
                    paymentAssistService.signVerify(payParam);
                    // 发起真实支付
                    NormalPayResult payResult = normalPayService.pay(payParam);
                    // 使用平台私钥对支付结果签名
                    String platformPrivateKey = platformConfigProperties.getKeyConfig().getPrivateKey();
                    String resultSign = PaySignUtil.sign(payResult, platformPrivateKey);
                    return new DevelopPayResult()
                            .setPayResult(payResult)
                            .setSign(resultSign);
                }
        );
    }

    /// 已启用渠道支付方式目录（供调试页支付方式下拉）
    public List<PayProviderMethodResult> listMethodDirectory() {
        return payProviderMethodService.listDirectoryFlat();
    }

    /// 传值模式：商户全部启用通道商户候选
    public List<LabelValue> listChannelMchCandidates(String mchNo) {
        return payRouteStrategyCapabilitySupport.listDirectChannelMchCandidates(mchNo);
    }

    /// 传值模式：按通道商户(产品)返回全部启用支付能力候选
    public List<LabelValue> listCapabilityCandidates(String channelMchNo) {
        return payRouteStrategyCapabilitySupport.listDirectCapabilityCandidates(channelMchNo);
    }
}
