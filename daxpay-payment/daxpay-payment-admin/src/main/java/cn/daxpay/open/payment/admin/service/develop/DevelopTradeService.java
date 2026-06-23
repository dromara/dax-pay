package cn.daxpay.open.payment.admin.service.develop;

import cn.daxpay.open.payment.admin.param.develop.DevelopParam;
import cn.daxpay.open.payment.admin.result.develop.DevelopPayResult;
import cn.daxpay.open.payment.admin.result.develop.DevelopSignResult;
import cn.daxpay.open.payment.common.context.PaymentScopeManager;
import cn.daxpay.open.payment.common.util.ObjectSignStrUtil;
import cn.daxpay.open.payment.common.util.PaySignUtil;
import cn.daxpay.open.payment.pay.service.NormalPayService;
import cn.daxpay.open.payment.unipay.param.trade.pay.PayParam;
import cn.daxpay.open.payment.unipay.result.trade.pay.PayResult;
import cn.daxpay.open.platform.common.json.util.JsonUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 交易开发调试服务
///
/// 复用普通支付核心服务 NormalPayService, 在管理端以指定商户/应用身份真实发起一笔支付,
/// 便于联调验证通道配置与签名是否正确
@Slf4j
@Service
@RequiredArgsConstructor
public class DevelopTradeService {

    private final NormalPayService normalPayService;
    private final PaymentScopeManager paymentScopeManager;

    /// 生成支付参数签名(与正式签名逻辑一致)
    public DevelopSignResult sign(DevelopParam<PayParam> param) {
        // 签名串(与 PaySignUtil 内部一致)
        String signStr = ObjectSignStrUtil.buildSignStr(param.getParam());
        // 签名值
        String sign = PaySignUtil.sign(param.getParam(), param.getPrivateKey());
        return new DevelopSignResult().setSignStr(signStr).setSign(sign);
    }

    /// 支付调试(真实发起)
    public DevelopPayResult pay(DevelopParam<PayParam> param) {
        PayParam payParam = param.getParam();
        // 记录发送的请求体
        String requestBody = JsonUtil.toJsonStr(payParam);
        // 签名信息(传入私钥时生成, 便于核对)
        DevelopSignResult signInfo = null;
        if (StrUtil.isNotBlank(param.getPrivateKey())) {
            signInfo = this.sign(param);
        }
        // 在支付作用域内执行, 初始化商户上下文, 使 mchNo/appId 等字段自动填充
        PayResult payResult = paymentScopeManager.executeWithScope(
                payParam.getMchNo(), payParam.getAppId(),
                () -> normalPayService.pay(payParam)
        );
        return new DevelopPayResult()
                .setRequestBody(requestBody)
                .setSignInfo(signInfo)
                .setPayResult(payResult);
    }
}
