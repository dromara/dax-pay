package cn.daxpay.open.channel.fuyou.strategy.product;

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

/// # 富友合作方支付产品策略
///
/// 声明富友合作方模式(fuyou_partner, spay 扫码标准接口「合作方」身位) + 微信/支付宝/银联 的 JSAPI/扫码/付款码。
///
/// 与服务商模式产品([FuyouProductStrategy], FUYOU_PAY)共用通道 fuyou_pay 与 spay 扫码标准接口族
/// (XML + GBK + MD5withRSA, /preCreate /wxPreCreate /micropay /commonQuery 等), 差异仅在:
/// 机构号(ins_cd)与 RSA 密钥对按产品独立配置(fuyou_isv_key_config 的 product=fuyou_partner 行),
/// 回调路径段为 `partner/*`。能力集与服务商模式一致。
@Slf4j
@Service
@RequiredArgsConstructor
public class FuyouPartnerProductStrategy extends AbsProductStrategy {

    /// 支付方式 → 支付能力映射
    private static final Map<PayMethodEnum, List<PayCapabilityEnum>> METHOD_CAP_MAP = Map.ofEntries(
            // 微信
            Map.entry(PayMethodEnum.WECHAT_JSAPI, List.of(PayCapabilityEnum.WECHAT_JSAPI)),
            Map.entry(PayMethodEnum.WECHAT_MINI, List.of(PayCapabilityEnum.WECHAT_MINI)),
            Map.entry(PayMethodEnum.WECHAT_QR, List.of(PayCapabilityEnum.WECHAT_QR)),
            Map.entry(PayMethodEnum.WECHAT_BARCODE, List.of(PayCapabilityEnum.WECHAT_BARCODE)),
            // 支付宝
            Map.entry(PayMethodEnum.ALIPAY_JSAPI, List.of(PayCapabilityEnum.ALIPAY_JSAPI)),
            Map.entry(PayMethodEnum.ALIPAY_QR, List.of(PayCapabilityEnum.ALIPAY_QR)),
            Map.entry(PayMethodEnum.ALIPAY_BARCODE, List.of(PayCapabilityEnum.ALIPAY_BARCODE)),
            // 银联
            Map.entry(PayMethodEnum.UNION_QR, List.of(PayCapabilityEnum.UNION_QR)));

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.FUYOU_PARTNER;
    }

    @Override
    public boolean isIsv() { return true; }

    @Override
    public boolean isTerminal() { return false; }

    @Override
    public boolean isSandbox() { return true; }

    @Override
    public ChannelApiCallMode getApiCallMode() { return ChannelApiCallMode.ISV; }

    @Override
    public ChannelPayIdType getPayIdType() { return ChannelPayIdType.MCH; }

    @Override
    public List<PayProviderEnum> supportedPayProviders() {
        return List.of(PayProviderEnum.WECHAT, PayProviderEnum.ALIPAY, PayProviderEnum.UNION_PAY);
    }

    @Override
    public Map<PayMethodEnum, List<PayCapabilityEnum>> methodCapabilityMapping() {
        return METHOD_CAP_MAP;
    }

    /// 富友合作方通道: 微信侧仅 JSAPI/小程序支付需要 appid(随单上送 sub_appid),
    /// 其余微信能力(扫码/付款码)与支付宝/银联能力无需绑定应用, 收窄弹窗候选与支付链路的解析范围
    @Override
    public Set<PayCapabilityEnum> wxAppRequiredCapabilities() {
        return Set.of(PayCapabilityEnum.WECHAT_JSAPI, PayCapabilityEnum.WECHAT_MINI);
    }
}
