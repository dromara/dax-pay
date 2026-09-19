package cn.daxpay.open.platform.core.enums.pay.channel;

import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/// # 支付通道枚举
///
/// 字典值: channel
@Getter
@RequiredArgsConstructor
public enum ChannelEnum implements I18nSupport {

    /// 支付宝
    ALIPAY("alipay"),
    /// 微信支付
    WECHAT("wechat"),
    /// 云闪付
    UNION_PAY("union_pay"),
    /// 乐刷
    LESHUA_PAY("leshua_pay"),
    /// 随行付
    VBILL_PAY("vbill_pay"),
    /// 汇付天下(下挂 ada_pay 与 dougong_pay 两个产品)
    HUIFU("huifu"),
    /// 海科融通
    HKRT_PAY("hkrt_pay"),
    /// 拉卡拉
    LAKALA_PAY("lakala_pay"),
    /// 富友
    FUYOU_PAY("fuyou_pay"),
    /// 盛付通
    SHENG_PAY("sheng_pay"),
    /// 银盛
    YSEP_PAY("ysep_pay"),
    /// 快钱
    QUICK_PAY("quick_pay"),
    /// 杉德
    SAND_PAY("sand_pay"),
    /// 易宝
    YEE_PAY("yee_pay"),
    /// 银联商务
    UMS_PAY("ums_pay"),
    /// 抖音支付
    DOUYIN("douyin"),
    /// Stripe
    STRIPE("stripe"),
    /// jeepay
    JEE_PAY("jee_pay"),
    /// 易支付(三方聚合平台)
    EASY_PAY("easy_pay"),
    /// 星驿付(新大陆旗下国通星驿)
    XINGYI_PAY("xingyi_pay"),
    /// 建行龙支付(建设银行聚合支付)
    LONG_PAY("long_pay"),
    /// 通联支付(聚合收单)
    ALLIN_PAY("allin_pay"),
    /// 收钱吧(聚合扫码收单)
    SHOUQIANBA("shouqianba"),
    ;

    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.channel";
    }

    /// 根据编码获取枚举
    public static ChannelEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                .orElse(null);
    }
}
