package cn.daxpay.open.channel.lakala.strategy.product;

import cn.daxpay.open.platform.core.enums.pay.channel.ChannelApiCallMode;
import cn.daxpay.open.platform.core.enums.pay.channel.ChannelPayIdType;
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

/// # 拉卡拉 MOSS 支付产品策略
///
/// 拉卡拉开放平台(MOSS)产品线, 与现有服务商模式(lakala_pay)同通道不同产品:
/// 统一网关 `ord-api/unified/v3`(服务 lfops.moss.*), SHA1withRSA 签名 + 报文 RSA 加解密。
///
/// 能力映射对齐主数据 pay_md_product_capability(lakala_moss 四能力):
/// wechat_qr / wechat_jsapi / alipay_qr / alipay_jsapi。
/// 凭证复用拉卡拉密钥配置体系(lakala_isv_key_config + lakala_isv_channel_merchant, 按 product 区分)。
@Slf4j
@Service
@RequiredArgsConstructor
public class LakalaMossProductStrategy extends AbsProductStrategy {

    private static final Map<PayMethodEnum, List<PayCapabilityEnum>> METHOD_CAP_MAP = Map.ofEntries(
            Map.entry(PayMethodEnum.WECHAT_QR, List.of(PayCapabilityEnum.WECHAT_QR)),
            Map.entry(PayMethodEnum.WECHAT_JSAPI, List.of(PayCapabilityEnum.WECHAT_JSAPI)),
            Map.entry(PayMethodEnum.ALIPAY_QR, List.of(PayCapabilityEnum.ALIPAY_QR)),
            Map.entry(PayMethodEnum.ALIPAY_JSAPI, List.of(PayCapabilityEnum.ALIPAY_JSAPI)));

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.LAKALA_MOSS;
    }

    @Override
    public boolean isIsv() { return true; }

    @Override
    public boolean isTerminal() { return false; }

    /// MOSS 无公开沙箱网关地址(沙箱/生产共用), 不开放沙箱环境
    @Override
    public boolean isSandbox() { return false; }

    @Override
    public ChannelApiCallMode getApiCallMode() { return ChannelApiCallMode.ISV; }

    @Override
    public ChannelPayIdType getPayIdType() { return ChannelPayIdType.MCH; }

    @Override
    public List<PayProviderEnum> supportedPayProviders() {
        return List.of(PayProviderEnum.WECHAT, PayProviderEnum.ALIPAY);
    }

    @Override
    public Map<PayMethodEnum, List<PayCapabilityEnum>> methodCapabilityMapping() {
        return METHOD_CAP_MAP;
    }

    /// MOSS 通道: 仅微信 JSAPI 支付需要 appid(上送 sub_appid),
    /// 其余能力(微信扫码收银台/支付宝)无需绑定应用, 收窄弹窗候选与支付链路的解析范围
    @Override
    public Set<PayCapabilityEnum> wxAppRequiredCapabilities() {
        return Set.of(PayCapabilityEnum.WECHAT_JSAPI);
    }
}
