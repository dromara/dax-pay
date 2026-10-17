package cn.daxpay.open.channel.baofu.strategy.product;

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
import java.util.Set;

/// # 宝付聚合支付产品策略
///
/// 宝付为聚合支付通道(聚合 juhe 网关), 单产品(BAOFU_PAY)承载微信/支付宝/云闪付多形态支付:
/// - 聚合收银台(AGGREGATE_QR): 一码多渠道(微信/支付宝), 通道侧自动识别底层渠道
/// - 微信: JSAPI/小程序/付款码
/// - 支付宝: 扫码/JS/付款码
/// - 云闪付: 扫码/云闪付 JS
///
/// 协议另有 WECHAT_APP(APP 支付)形态, 参考实现未覆盖其响应解析(仅 JS 场景读 wc_pay_data), 一期不开放。
@Slf4j
@Service
@RequiredArgsConstructor
public class BaofuDirectProductStrategy extends AbsProductStrategy {

    /// 支付方式 → 支付能力映射
    private static final Map<PayMethodEnum, List<PayCapabilityEnum>> METHOD_CAP_MAP = Map.ofEntries(
            Map.entry(PayMethodEnum.AGGREGATE_PAY_QRCODE, List.of(PayCapabilityEnum.AGGREGATE_PAY_QRCODE)),
            Map.entry(PayMethodEnum.WECHAT_QR, List.of(PayCapabilityEnum.WECHAT_QR)),
            Map.entry(PayMethodEnum.WECHAT_JSAPI, List.of(PayCapabilityEnum.WECHAT_JSAPI)),
            Map.entry(PayMethodEnum.WECHAT_MINI, List.of(PayCapabilityEnum.WECHAT_MINI)),
            Map.entry(PayMethodEnum.WECHAT_BARCODE, List.of(PayCapabilityEnum.WECHAT_BARCODE)),
            Map.entry(PayMethodEnum.ALIPAY_QR, List.of(PayCapabilityEnum.ALIPAY_QR)),
            Map.entry(PayMethodEnum.ALIPAY_JSAPI, List.of(PayCapabilityEnum.ALIPAY_JSAPI)),
            Map.entry(PayMethodEnum.ALIPAY_BARCODE, List.of(PayCapabilityEnum.ALIPAY_BARCODE)),
            Map.entry(PayMethodEnum.UNION_QR, List.of(PayCapabilityEnum.UNION_QR)),
            Map.entry(PayMethodEnum.UNION_JSAPI, List.of(PayCapabilityEnum.UNION_JSAPI)));

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.BAOFU_PAY;
    }

    @Override
    public boolean isTerminal() {
        return true;
    }

    /// 宝付聚合线无公开独立沙箱网关
    @Override
    public boolean isSandbox() {
        return false;
    }

    @Override
    public List<PayProviderEnum> supportedPayProviders() {
        // AGGREGATE_PAY: 通道路由基础模式可按聚合扫码 method 绑定本产品
        return List.of(PayProviderEnum.AGGREGATE_PAY, PayProviderEnum.WECHAT, PayProviderEnum.ALIPAY,
                PayProviderEnum.UNION_PAY);
    }

    @Override
    public Map<PayMethodEnum, List<PayCapabilityEnum>> methodCapabilityMapping() {
        return METHOD_CAP_MAP;
    }

    /// 宝付聚合直连: 微信侧仅 JSAPI/小程序支付需要 appid(payExtend.sub_appid),
    /// 其余微信能力(聚合扫码/付款码等)无需绑定应用, 收窄弹窗候选与支付链路的解析范围
    @Override
    public Set<PayCapabilityEnum> wxAppRequiredCapabilities() {
        return Set.of(PayCapabilityEnum.WECHAT_JSAPI, PayCapabilityEnum.WECHAT_MINI);
    }
}
