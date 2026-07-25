package cn.daxpay.open.payment.merchant.service.develop;

import cn.daxpay.open.payment.common.util.ObjectSignStrUtil;
import cn.daxpay.open.payment.common.util.PaySignUtil;
import cn.daxpay.open.payment.merchant.param.develop.DevelopParam;
import cn.daxpay.open.payment.merchant.result.develop.DevelopSignResult;
import cn.daxpay.open.payment.unipay.param.gateway.GatewayPrePayParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 网关支付开发调试服务
///
/// 仅提供组参辅助与签名能力, **不发起真实交易**。
/// 真实预下单由商户端调试页模拟商户 HTTP 请求调用 `/unipay/gateway/pre-pay` 完成,
/// 与正式对接走同一入口, 避免内部直调支付核心形成后门。
@Slf4j
@Service
@RequiredArgsConstructor
public class DevelopGatewayService {

    /// 生成网关预下单参数签名(与正式签名逻辑一致)
    ///
    /// 调用方需在 param 中自备 reqTime / nonceStr 等公共字段, 本方法只负责签名串与签名值。
    public DevelopSignResult sign(DevelopParam<GatewayPrePayParam> param) {
        // 签名串(与 PaySignUtil 内部一致)
        String signStr = ObjectSignStrUtil.buildSignStr(param.getParam());
        // 签名值
        String sign = PaySignUtil.sign(param.getParam(), param.getPrivateKey());
        return new DevelopSignResult().setSignStr(signStr).setSign(sign);
    }
}
