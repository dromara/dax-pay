package cn.daxpay.open.channel.mock.strategy;

import cn.daxpay.open.platform.core.enums.pay.channel.PayCapabilityEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.PayProviderEnum;

import java.util.List;
import java.util.Map;

/// # Mock 通道产品能力目录(单一事实源)
///
/// 一通道双产品([cn.daxpay.open.channel.mock.strategy.product.MockDirectProductStrategy] 直连 /
/// [cn.daxpay.open.channel.mock.strategy.product.MockIsvProductStrategy] 服务商)的
/// 方式→能力声明与渠道声明统一收口在本类, 能力枚举名与支付方式枚举名同名, 与主数据
/// `pay_md_product_capability`(mock_pay 21 项)逐项对齐。
///
/// 覆盖 open 平台四家钱包的全部真实支付方式(21 项, 微信 7 / 支付宝 6 / 银联 4 / 抖音 4):
/// 无固定渠道(OTHER)、数字人民币(ECNY)与卡组(VISA/MASTERCARD)不属模拟通道范围, 不纳入。
/// 聚合扫码([PayMethodEnum#AGGREGATE_PAY_QRCODE])本产品不声明能力(SQL 未挂该能力),
/// 但 [cn.daxpay.open.channel.mock.service.MockPayService#mapWay] 仍保留其映射备用。
///
/// 与子应用 way 的对应关系见 [cn.daxpay.open.channel.mock.service.MockPayService] 的 mapWay,
/// 两侧一致性由 MockPayMethodAlignmentTest 守住。
public final class MockProductCapabilityCatalog {

    /// 支付方式 → 支付能力映射(能力枚举名与方式枚举名同名)
    public static final Map<PayMethodEnum, List<PayCapabilityEnum>> METHOD_CAP_MAP = Map.ofEntries(
            // 微信(小程序收银台/扫码/JSAPI/小程序/H5/APP/付款码)
            Map.entry(PayMethodEnum.WECHAT_CASHIER, List.of(PayCapabilityEnum.WECHAT_CASHIER)),
            Map.entry(PayMethodEnum.WECHAT_QR, List.of(PayCapabilityEnum.WECHAT_QR)),
            Map.entry(PayMethodEnum.WECHAT_JSAPI, List.of(PayCapabilityEnum.WECHAT_JSAPI)),
            Map.entry(PayMethodEnum.WECHAT_MINI, List.of(PayCapabilityEnum.WECHAT_MINI)),
            Map.entry(PayMethodEnum.WECHAT_H5, List.of(PayCapabilityEnum.WECHAT_H5)),
            Map.entry(PayMethodEnum.WECHAT_APP, List.of(PayCapabilityEnum.WECHAT_APP)),
            Map.entry(PayMethodEnum.WECHAT_BARCODE, List.of(PayCapabilityEnum.WECHAT_BARCODE)),
            // 支付宝(扫码/JSAPI/电脑网站/H5/APP/付款码)
            Map.entry(PayMethodEnum.ALIPAY_QR, List.of(PayCapabilityEnum.ALIPAY_QR)),
            Map.entry(PayMethodEnum.ALIPAY_JSAPI, List.of(PayCapabilityEnum.ALIPAY_JSAPI)),
            Map.entry(PayMethodEnum.ALIPAY_PC, List.of(PayCapabilityEnum.ALIPAY_PC)),
            Map.entry(PayMethodEnum.ALIPAY_H5, List.of(PayCapabilityEnum.ALIPAY_H5)),
            Map.entry(PayMethodEnum.ALIPAY_APP, List.of(PayCapabilityEnum.ALIPAY_APP)),
            Map.entry(PayMethodEnum.ALIPAY_BARCODE, List.of(PayCapabilityEnum.ALIPAY_BARCODE)),
            // 银联(扫码/JSAPI/H5/付款码, 平台无 APP 方式)
            Map.entry(PayMethodEnum.UNION_QR, List.of(PayCapabilityEnum.UNION_QR)),
            Map.entry(PayMethodEnum.UNION_JSAPI, List.of(PayCapabilityEnum.UNION_JSAPI)),
            Map.entry(PayMethodEnum.UNION_H5, List.of(PayCapabilityEnum.UNION_H5)),
            Map.entry(PayMethodEnum.UNION_BARCODE, List.of(PayCapabilityEnum.UNION_BARCODE)),
            // 抖音(扫码/JSAPI/H5/APP, 平台无付款码方式)
            Map.entry(PayMethodEnum.DOUYIN_QR, List.of(PayCapabilityEnum.DOUYIN_QR)),
            Map.entry(PayMethodEnum.DOUYIN_JSAPI, List.of(PayCapabilityEnum.DOUYIN_JSAPI)),
            Map.entry(PayMethodEnum.DOUYIN_H5, List.of(PayCapabilityEnum.DOUYIN_H5)),
            Map.entry(PayMethodEnum.DOUYIN_APP, List.of(PayCapabilityEnum.DOUYIN_APP)));

    /// 通道路由可用的支付渠道(基础模式按渠道默认绑定时的候选范围)
    public static final List<PayProviderEnum> SUPPORTED_PROVIDERS = List.of(
            // 聚合支付: 聚合扫码方式的路由渠道(与真实聚合产品一致, 聚合居首)
            PayProviderEnum.AGGREGATE_PAY,
            PayProviderEnum.WECHAT,
            PayProviderEnum.ALIPAY,
            PayProviderEnum.UNION_PAY,
            PayProviderEnum.DOUYIN);

    private MockProductCapabilityCatalog() {
    }
}
