package cn.daxpay.open.platform.core.enums.pay.channel;

import cn.daxpay.open.platform.core.exception.BizException;
import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/// # 支付产品枚举
///
/// 字典值: pay_product
@Getter
@RequiredArgsConstructor
public enum ProductEnum implements I18nSupport {

    // ===== 支付宝 =====
    /// 支付宝(服务商)
    ALIPAY_ISV("alipay_isv"),
    /// 支付宝(直连)
    ALIPAY("alipay"),

    // ===== 微信支付 =====
    /// 微信支付(服务商)
    WECHAT_ISV("wechat_isv"),
    /// 微信支付(直连)
    WECHAT_PAY("wechat_pay"),

    // ===== 抖音支付 =====
    /// 抖音支付(直连)
    DOUYIN_PAY("douyin_pay"),

    // ===== Stripe =====
    /// Stripe 支付(Visa/MasterCard 等国际卡)
    STRIPE_PAY("stripe_pay"),

    // ===== 银联商务 =====
    /// 银联商务(C扫B)
    UMS_QRCODE("ums_qrcode"),
    /// 银联商务(公众号)
    UMS_JSAPI("ums_jsapi"),
    /// 银联商务(APP)
    UMS_APP("ums_app"),
    /// 银联商务(小程序)
    UMS_MINI("ums_mini"),
    /// 银联商务(H5)
    UMS_H5("ums_h5"),
    /// 银联商务(B扫C)
    UMS_BARCODE("ums_barcode"),

    // ===== 云闪付(银联 ACP) =====
    /// 云闪付(单一产品, 含主扫 UNION_QR / H5 / 被扫 UNION_BARCODE 三种支付方式)
    UNION_PAY("union_pay"),

    // ===== 拉卡拉 =====
    /// 拉卡拉支付
    LAKALA_PAY("lakala_pay"),

    // ===== 乐刷 =====
    /// 乐刷支付
    LESHUA_PAY("leshua_pay"),

    // ===== 第三方聚合通道（一通道一产品）=====
    /// Adapay(直连, 归属汇付天下通道 huifu)
    ADA_PAY("ada_pay"),
    /// 斗拱(归属汇付天下通道 huifu)
    DOUGONG_PAY("dougong_pay"),
    /// 海科融通
    HKRT_PAY("hkrt_pay"),
    /// 随行付
    VBILL_PAY("vbill_pay"),
    /// 富友
    FUYOU_PAY("fuyou_pay"),
    /// 盛付通
    SHENG_PAY("sheng_pay"),
    /// 银盛
    YSEP_PAY("ysep_pay"),
    /// 快钱
    QUICK_PAY("quick_pay"),
    /// 河马付(杉德旗下产品)
    HM_PAY("hm_pay"),
    /// 易宝
    YEE_PAY("yee_pay"),
    /// jeepay
    JEE_PAY("jee_pay"),
    ;

    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.product";
    }

    /// 获取所属通道编码
    public String getChannel() {
        return switch (this) {
            case ALIPAY_ISV, ALIPAY -> ChannelEnum.ALIPAY.getCode();
            case WECHAT_ISV, WECHAT_PAY -> ChannelEnum.WECHAT.getCode();
            // 银联商务系列
            case UMS_QRCODE, UMS_JSAPI, UMS_APP, UMS_MINI, UMS_H5, UMS_BARCODE -> ChannelEnum.UMS_PAY.getCode();
            // 云闪付(银联 ACP)
            case UNION_PAY -> ChannelEnum.UNION_PAY.getCode();
            // 拉卡拉
            case LAKALA_PAY -> ChannelEnum.LAKALA_PAY.getCode();
            // 乐刷
            case LESHUA_PAY -> ChannelEnum.LESHUA_PAY.getCode();
            // 抖音支付(通道 code 为 douyin，产品为 douyin_pay)
            case DOUYIN_PAY -> ChannelEnum.DOUYIN.getCode();
            // Stripe(通道 code 为 stripe，产品为 stripe_pay)
            case STRIPE_PAY -> ChannelEnum.STRIPE.getCode();
            // 汇付天下: Adapay / 斗拱 两产品共用通道 huifu
            case ADA_PAY, DOUGONG_PAY -> ChannelEnum.HUIFU.getCode();
            case HKRT_PAY -> ChannelEnum.HKRT_PAY.getCode();
            case VBILL_PAY -> ChannelEnum.VBILL_PAY.getCode();
            case FUYOU_PAY -> ChannelEnum.FUYOU_PAY.getCode();
            case SHENG_PAY -> ChannelEnum.SHENG_PAY.getCode();
            case YSEP_PAY -> ChannelEnum.YSEP_PAY.getCode();
            case QUICK_PAY -> ChannelEnum.QUICK_PAY.getCode();
            // 河马付归属杉德通道
            case HM_PAY -> ChannelEnum.SAND_PAY.getCode();
            case YEE_PAY -> ChannelEnum.YEE_PAY.getCode();
            case JEE_PAY -> ChannelEnum.JEE_PAY.getCode();
        };
    }

    /// 根据编码获取枚举
    public static ProductEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                // 通用: 未知的支付产品
                .orElseThrow(() -> new BizException("error.common.enumUnknown", code));
    }
}
