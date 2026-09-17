package cn.daxpay.open.channel.sheng.strategy.product;

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

/// # 盛付通服务商(ISV)支付产品策略
///
/// 盛付通聚合支付服务商模式(SHENG_ISV, 与商户模式 SHENG_PAY 同属盛付通通道),
/// 服务商凭证产品级全局一份, 通道商户维度仅绑定子商户号, 代子商户发起交易。
/// 微信/支付宝/银联三钱包 15 种支付方式。
///
/// 注意: SQL 种子(pay_md_product_capability)含 16 项能力, 其中 union_h5 一期不接入
/// (upacp 枚举无 wap 映射, 2026-09-17 拍板先不处理), 本映射不含此项;
/// 若商户配置了该能力路由, 支付时在支付方式映射处报不支持。
///
/// 盛付通不提供集测接口(2026-09-17 官方二次确认), 不支持沙箱环境, 全部交易走生产网关。
@Slf4j
@Service
@RequiredArgsConstructor
public class ShengIsvProductStrategy extends AbsProductStrategy {

    private static final Map<PayMethodEnum, List<PayCapabilityEnum>> METHOD_CAP_MAP = Map.ofEntries(
            // 微信
            Map.entry(PayMethodEnum.WECHAT_QR, List.of(PayCapabilityEnum.WECHAT_QR)),
            Map.entry(PayMethodEnum.WECHAT_JSAPI, List.of(PayCapabilityEnum.WECHAT_JSAPI)),
            Map.entry(PayMethodEnum.WECHAT_MINI, List.of(PayCapabilityEnum.WECHAT_MINI)),
            Map.entry(PayMethodEnum.WECHAT_APP, List.of(PayCapabilityEnum.WECHAT_APP)),
            Map.entry(PayMethodEnum.WECHAT_H5, List.of(PayCapabilityEnum.WECHAT_H5)),
            Map.entry(PayMethodEnum.WECHAT_BARCODE, List.of(PayCapabilityEnum.WECHAT_BARCODE)),
            // 支付宝(无独立 mini method, 小程序场景统一 alipay_jsapi)
            Map.entry(PayMethodEnum.ALIPAY_QR, List.of(PayCapabilityEnum.ALIPAY_QR)),
            Map.entry(PayMethodEnum.ALIPAY_JSAPI, List.of(PayCapabilityEnum.ALIPAY_JSAPI)),
            Map.entry(PayMethodEnum.ALIPAY_APP, List.of(PayCapabilityEnum.ALIPAY_APP)),
            Map.entry(PayMethodEnum.ALIPAY_H5, List.of(PayCapabilityEnum.ALIPAY_H5)),
            Map.entry(PayMethodEnum.ALIPAY_PC, List.of(PayCapabilityEnum.ALIPAY_PC)),
            Map.entry(PayMethodEnum.ALIPAY_BARCODE, List.of(PayCapabilityEnum.ALIPAY_BARCODE)),
            // 银联(union_h5 一期不接入, 种子保留不动)
            Map.entry(PayMethodEnum.UNION_QR, List.of(PayCapabilityEnum.UNION_QR)),
            Map.entry(PayMethodEnum.UNION_JSAPI, List.of(PayCapabilityEnum.UNION_JSAPI)),
            Map.entry(PayMethodEnum.UNION_BARCODE, List.of(PayCapabilityEnum.UNION_BARCODE)));

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.SHENG_ISV;
    }

    @Override
    public boolean isIsv() { return true; }

    @Override
    public boolean isTerminal() { return false; }

    /// 盛付通不提供集测接口(官方二次确认, 2026-09-17 撤回沙箱支持), 恒为生产环境
    @Override
    public boolean isSandbox() { return false; }

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
}
