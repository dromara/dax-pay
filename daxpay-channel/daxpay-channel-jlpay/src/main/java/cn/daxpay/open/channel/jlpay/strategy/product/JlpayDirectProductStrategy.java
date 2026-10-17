package cn.daxpay.open.channel.jlpay.strategy.product;

import cn.daxpay.open.platform.core.enums.pay.channel.PayCapabilityEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.PayProviderEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.payment.strategy.product.AbsProductStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/// # 嘉联聚合支付产品策略
///
/// 嘉联为聚合支付通道(OpenAPI 开放平台), 单产品(JL_PAY)承载微信/支付宝/云闪付:
/// - 扫码主扫(qrcodepay): 按 pay_type 出对应渠道二维码
/// - 付款码被扫(micropay): 凭 auth_code 由嘉联自动识别渠道
///
/// JSAPI/小程序/APP/收银托管等形态(officialpay/waph5pay/cashierpay)需 openId/微信应用绑定体系, 一期未接。
@Slf4j
@Service
@RequiredArgsConstructor
public class JlpayDirectProductStrategy extends AbsProductStrategy {

    /// 支付方式 → 支付能力映射
    private static final Map<PayMethodEnum, List<PayCapabilityEnum>> METHOD_CAP_MAP = Map.ofEntries(
            Map.entry(PayMethodEnum.WECHAT_QR, List.of(PayCapabilityEnum.WECHAT_QR)),
            Map.entry(PayMethodEnum.ALIPAY_QR, List.of(PayCapabilityEnum.ALIPAY_QR)),
            Map.entry(PayMethodEnum.UNION_QR, List.of(PayCapabilityEnum.UNION_QR)),
            Map.entry(PayMethodEnum.WECHAT_BARCODE, List.of(PayCapabilityEnum.WECHAT_BARCODE)),
            Map.entry(PayMethodEnum.ALIPAY_BARCODE, List.of(PayCapabilityEnum.ALIPAY_BARCODE)),
            Map.entry(PayMethodEnum.UNION_BARCODE, List.of(PayCapabilityEnum.UNION_BARCODE)));

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.JL_PAY;
    }

    @Override
    public boolean isTerminal() {
        return true;
    }

    /// 嘉联提供 UAT 测试网关(openapi-uat.jlpay.com), 支持沙箱联调
    @Override
    public boolean isSandbox() {
        return true;
    }

    @Override
    public List<PayProviderEnum> supportedPayProviders() {
        return List.of(PayProviderEnum.WECHAT, PayProviderEnum.ALIPAY, PayProviderEnum.UNION_PAY);
    }

    @Override
    public Map<PayMethodEnum, List<PayCapabilityEnum>> methodCapabilityMapping() {
        return METHOD_CAP_MAP;
    }
}
