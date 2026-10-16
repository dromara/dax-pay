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
    /// 拉卡拉
    LAKALA_PAY("lakala_pay"),

    // ===== 乐刷 =====
    /// 乐刷支付
    LESHUA_PAY("leshua_pay"),

    // ===== 第三方聚合通道（一通道一产品）=====
    /// Adapay(直连, 归属汇付天下通道 huifu)
    ADA_PAY("ada_pay"),
    /// 斗拱(归属汇付天下通道 huifu)
    DOUGONG_PAY("dougong_pay"),
    /// 汇付数字人民币(经斗拱)
    HUIFU_ECNY("huifu_ecny"),
    /// 海科融通
    HKRT_PAY("hkrt_pay"),
    /// 随行付
    VBILL_PAY("vbill_pay"),
    /// 富友
    FUYOU_PAY("fuyou_pay"),
    /// 富友商户前置(直连商户模式, 商户移动支付前置接口)
    FUYOU_DIRECT("fuyou_direct"),
    /// 富友(互联网直连)
    FUYOU_INTERNET("fuyou_internet"),
    /// 富友(H5聚合收银台)
    FUYOU_H5("fuyou_h5"),
    /// 盛付通(商户模式, 商户自有对接密钥)
    SHENG_PAY("sheng_pay"),
    /// 盛付通(服务商模式, 代子商户发起交易)
    SHENG_ISV("sheng_isv"),
    /// 银盛
    YSEP_PAY("ysep_pay"),
    /// 银盛e企通
    YSEQT_PAY("yseqt_pay"),
    /// 快钱
    QUICK_PAY("quick_pay"),
    /// 河马付(杉德旗下产品)
    HM_PAY("hm_pay"),
    /// 杉德支付(杉德通道主产品)
    SAND_PAY("sand_pay"),
    /// 易宝
    YEE_PAY("yee_pay"),
    /// jeepay
    JEE_PAY("jee_pay"),
    /// 易支付(三方聚合平台, 一通道一产品, 一期仅扫码两类)
    EASY_PAY("easy_pay"),
    /// 星驿付(新大陆旗下国通星驿, 一通道一产品)
    XINGYI_PAY("xingyi_pay"),
    /// 建行龙支付(建设银行聚合支付, 一通道一产品)
    LONG_PAY("long_pay"),
    // ===== 通联支付(四产品共用通道 allin_pay, 均可收单, 支持沙箱) =====
    /// 通联支付·云商通(综合支付版, 一站式聚合收单)
    ALLIN_CLOUD("allin_cloud"),
    /// 通联支付·收银宝(收单业务, 统一下单 API)
    ALLIN_CASHIER("allin_cashier"),
    /// 通联支付·收付通(收付业务, 收款+付款)
    ALLIN_PAYFUND("allin_payfund"),
    /// 通联支付·金服宝(直联业务, AT 直连收单)
    ALLIN_DIRECT("allin_direct"),
    /// 收钱吧(聚合扫码收单, 一通道一产品)
    SHOUQIANBA("shouqianba"),

    // ===== 合规通道占位(占位产品, 未对接, 仅枚举与目录层登记, 无策略实现) =====
    /// 连连聚合支付(占位, 未对接)
    LLIAN_PAY("llian_pay"),
    /// 汇聚聚合支付(占位, 未对接)
    JOIN_PAY("join_pay"),
    /// 威富通支付(占位, 未对接)
    SWIFT_PASS("swift_pass"),
    /// 嘉联聚合支付(占位, 未对接)
    JL_PAY("jl_pay"),
    /// 合利宝支付(占位, 未对接)
    HELI_PAY("heli_pay"),
    /// 付呗聚合支付(占位, 未对接)
    FUBEI_PAY("fubei_pay"),
    /// 易票联支付(占位, 未对接)
    EPAY_LINKS("epay_links"),
    /// PayPal 支付(国际, 占位未对接)
    PAY_PAL("paypal"),
    /// Airwallex 支付(国际, 占位未对接)
    AIRWALLEX("airwallex"),
    /// 中金聚合支付(占位, 未对接)
    CPCN_PAY("cpcn_pay"),
    /// 宝付支付(占位, 未对接)
    BAOFU_PAY("baofu_pay"),
    /// 首信易支付(占位, 未对接)
    YIZHIFU_PAY("yizhifubj_pay"),
    /// 哆啦宝支付(占位, 未对接)
    DUOLABAO_PAY("duolabao_pay"),
    /// 火脸支付(占位, 未对接)
    HUOLIAN_PAY("huolian_pay"),
    /// 有赞支付(占位, 未对接)
    YOUZAN_PAY("youzan_pay"),
    /// 银盈通支付(占位, 未对接)
    YYT_PAY("yinyingtong_pay"),
    /// 慧收钱(占位, 未对接)
    HSQ_PAY("huishouqian_pay"),
    /// 收银呗支付(占位, 未对接)
    SYB_PAY("shouyinbei_pay"),
    /// Payssion(占位, 未对接)
    PAYSSION("payssion"),
    /// 拉卡拉MOSS(拉卡拉 MOSS 开放平台产品线, 占位未对接)
    LAKALA_MOSS("lakala_moss"),
    /// 富友合作方(scanpay 合作方模式, 占位未对接)
    FUYOU_PARTNER("fuyou_partner"),
    /// 海科聚合支付(海科融通 SaaS 聚合产品线, 占位未对接)
    HKRT_SAAS("hkrt_saas"),
    /// 新生支付(占位, 未对接)
    HNA_PAY("hnapay_pay"),
    /// 新生易(新生支付旗下机构模式聚合产品线, 占位未对接)
    XSY_PAY("xsy"),
    /// 联动优势(占位, 未对接)
    UMF_PAY("umf_pay"),
    /// 易生支付(占位, 未对接)
    YISHENG_PAY("yisheng_pay"),
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
            // 拉卡拉: 聚合收单(lakala_pay)/MOSS(lakala_moss) 两产品共用通道 lakala_pay
            case LAKALA_PAY, LAKALA_MOSS -> ChannelEnum.LAKALA_PAY.getCode();
            // 乐刷
            case LESHUA_PAY -> ChannelEnum.LESHUA_PAY.getCode();
            // 抖音支付(通道 code 为 douyin，产品为 douyin_pay)
            case DOUYIN_PAY -> ChannelEnum.DOUYIN.getCode();
            // Stripe(通道 code 为 stripe，产品为 stripe_pay)
            case STRIPE_PAY -> ChannelEnum.STRIPE.getCode();
            // 汇付天下: Adapay / 斗拱 两产品共用通道 huifu
            case ADA_PAY, DOUGONG_PAY -> ChannelEnum.HUIFU.getCode();
            // 汇付数字人民币(经斗拱)归属汇付天下通道
            case HUIFU_ECNY -> ChannelEnum.HUIFU.getCode();
            // 海科融通: 收单(hkrt_pay)/SaaS聚合(hkrt_saas) 两产品共用通道 hkrt_pay
            case HKRT_PAY, HKRT_SAAS -> ChannelEnum.HKRT_PAY.getCode();
            case VBILL_PAY -> ChannelEnum.VBILL_PAY.getCode();
            // 富友: 服务商(fuyou_pay)/商户前置(fuyou_direct)/互联网直连(fuyou_internet)/H5聚合收银台(fuyou_h5)/合作方(fuyou_partner) 多产品共用通道 fuyou_pay
            case FUYOU_PAY, FUYOU_DIRECT, FUYOU_INTERNET, FUYOU_H5, FUYOU_PARTNER -> ChannelEnum.FUYOU_PAY.getCode();
            // 盛付通: 商户(sheng_pay)/服务商(sheng_isv) 两产品共用通道 sheng_pay
            case SHENG_PAY, SHENG_ISV -> ChannelEnum.SHENG_PAY.getCode();
            // 银盛
            case YSEP_PAY -> ChannelEnum.YSEP_PAY.getCode();
            // 银盛e企通归属银盛通道
            case YSEQT_PAY -> ChannelEnum.YSEP_PAY.getCode();
            case QUICK_PAY -> ChannelEnum.QUICK_PAY.getCode();
            // 河马付归属杉德通道
            case HM_PAY -> ChannelEnum.SAND_PAY.getCode();
            // 杉德主产品归属杉德通道
            case SAND_PAY -> ChannelEnum.SAND_PAY.getCode();
            case YEE_PAY -> ChannelEnum.YEE_PAY.getCode();
            case JEE_PAY -> ChannelEnum.JEE_PAY.getCode();
            // 易支付: 一通道一产品
            case EASY_PAY -> ChannelEnum.EASY_PAY.getCode();
            // 星驿付: 一通道一产品
            case XINGYI_PAY -> ChannelEnum.XINGYI_PAY.getCode();
            // 建行龙支付: 一通道一产品
            case LONG_PAY -> ChannelEnum.LONG_PAY.getCode();
            // 通联支付: 云商通/收银宝/收付通/金服宝 四产品共用通道 allin_pay
            case ALLIN_CLOUD, ALLIN_CASHIER, ALLIN_PAYFUND, ALLIN_DIRECT -> ChannelEnum.ALLIN_PAY.getCode();
            // 收钱吧: 一通道一产品
            case SHOUQIANBA -> ChannelEnum.SHOUQIANBA.getCode();
            // ===== 合规通道占位: 一通道一产品, 未对接 =====
            // 连连支付(占位通道, 未对接)
            case LLIAN_PAY -> ChannelEnum.LLIAN_PAY.getCode();
            // 汇聚支付(占位通道, 未对接)
            case JOIN_PAY -> ChannelEnum.JOIN_PAY.getCode();
            // 威富通(占位通道, 未对接)
            case SWIFT_PASS -> ChannelEnum.SWIFT_PASS.getCode();
            // 嘉联支付(占位通道, 未对接)
            case JL_PAY -> ChannelEnum.JL_PAY.getCode();
            // 合利宝(占位通道, 未对接)
            case HELI_PAY -> ChannelEnum.HELI_PAY.getCode();
            // 付呗(占位通道, 未对接)
            case FUBEI_PAY -> ChannelEnum.FUBEI_PAY.getCode();
            // 易票联(占位通道, 未对接)
            case EPAY_LINKS -> ChannelEnum.EPAY_LINKS.getCode();
            // PayPal(国际通道, 占位未对接)
            case PAY_PAL -> ChannelEnum.PAY_PAL.getCode();
            // Airwallex(国际通道, 占位未对接)
            case AIRWALLEX -> ChannelEnum.AIRWALLEX.getCode();
            // ===== 合规通道占位 C 批: 未对接 =====
            // 中金支付(占位通道, 未对接)
            case CPCN_PAY -> ChannelEnum.CPCN_PAY.getCode();
            // 宝付支付(占位通道, 未对接)
            case BAOFU_PAY -> ChannelEnum.BAOFU_PAY.getCode();
            // 首信易支付(占位通道, 未对接)
            case YIZHIFU_PAY -> ChannelEnum.YIZHIFU_PAY.getCode();
            // 哆啦宝(占位通道, 未对接)
            case DUOLABAO_PAY -> ChannelEnum.DUOLABAO_PAY.getCode();
            // 火脸支付(占位通道, 未对接)
            case HUOLIAN_PAY -> ChannelEnum.HUOLIAN_PAY.getCode();
            // 有赞支付(占位通道, 未对接)
            case YOUZAN_PAY -> ChannelEnum.YOUZAN_PAY.getCode();
            // 银盈通(占位通道, 未对接)
            case YYT_PAY -> ChannelEnum.YYT_PAY.getCode();
            // 慧收钱(占位通道, 未对接)
            case HSQ_PAY -> ChannelEnum.HSQ_PAY.getCode();
            // 收银呗(占位通道, 未对接)
            case SYB_PAY -> ChannelEnum.SYB_PAY.getCode();
            // Payssion(占位通道, 未对接)
            case PAYSSION -> ChannelEnum.PAYSSION.getCode();
            // ===== D/E 批占位: 未对接 =====
            // 新生支付: 直连(hnapay_pay)/新生易(xsy) 两产品共用通道 hnapay
            case HNA_PAY, XSY_PAY -> ChannelEnum.HNA_PAY.getCode();
            // 联动优势: 一通道一产品
            case UMF_PAY -> ChannelEnum.UMF_PAY.getCode();
            // 易生支付: 一通道一产品(与易支付协议 easy_pay 无关)
            case YISHENG_PAY -> ChannelEnum.YISHENG_PAY.getCode();
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

    /// 根据编码获取枚举；未知编码返回 null(导出翻译等容错场景使用, 防止单行脏数据打崩整次导出)
    public static ProductEnum findByCodeOrNull(String code) {
        if (code == null) {
            return null;
        }
        return Arrays.stream(values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                .orElse(null);
    }
}
