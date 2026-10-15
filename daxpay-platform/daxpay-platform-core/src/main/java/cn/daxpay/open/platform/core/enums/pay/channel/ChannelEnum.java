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

    // ===== 合规通道占位(占位通道, 未对接, 仅枚举与目录层登记, 无策略实现与创建入口) =====
    /// 连连支付(占位通道, 未对接)
    LLIAN_PAY("llianpay"),
    /// 汇聚支付(占位通道, 未对接)
    JOIN_PAY("joinpay"),
    /// 威富通(占位通道, 未对接)
    SWIFT_PASS("swiftpass"),
    /// 嘉联支付(占位通道, 未对接)
    JL_PAY("jlpay"),
    /// 合利宝(占位通道, 未对接)
    HELI_PAY("helipay"),
    /// 付呗(占位通道, 未对接)
    FUBEI_PAY("fubei"),
    /// 易票联(占位通道, 未对接)
    EPAY_LINKS("epaylinks"),
    /// PayPal(国际支付通道, 占位未对接)
    PAY_PAL("paypal"),
    /// Airwallex(国际支付通道, 占位未对接)
    AIRWALLEX("airwallex"),
    /// 中金支付(占位通道, 未对接)
    CPCN_PAY("cpcn"),
    /// 宝付支付(占位通道, 未对接)
    BAOFU_PAY("baofu"),
    /// 丰付支付(占位通道, 未对接)
    SUMA_PAY("sumapay"),
    /// 首信易支付(占位通道, 未对接)
    YIZHIFU_PAY("yizhifubj"),
    /// 汇付宝(占位通道, 未对接)
    HEEPAY_PAY("heepay"),
    /// 哆啦宝(占位通道, 未对接)
    DUOLABAO_PAY("duolabao"),
    /// 精秀支付(占位通道, 未对接)
    PASSPAY("passpay"),
    /// 鲲鹏支付(占位通道, 未对接)
    KUNPENG_PAY("kunpeng"),
    /// 联拓支付(占位通道, 未对接)
    LIANTUOFU_PAY("liantuofu"),
    /// 火脸支付(占位通道, 未对接)
    HUOLIAN_PAY("huolian"),
    /// 有赞支付(占位通道, 未对接)
    YOUZAN_PAY("youzan"),
    /// 移领支付(占位通道, 未对接)
    YLPAY("ylpay"),
    /// 远付宝(占位通道, 未对接)
    YUANFUBAO_PAY("yuanfubao"),
    /// 通莞金服(占位通道, 未对接)
    TOGOODFIN_PAY("togoodfin"),
    /// 乐收付(占位通道, 未对接)
    SOEZPAY("soezpay"),
    /// 数汇联动(占位通道, 未对接)
    SHUHUI_PAY("shuhuipay"),
    /// 红小点支付(占位通道, 未对接)
    XHD_PAY("xhdpay"),
    /// 恒信通(占位通道, 未对接)
    HXT_PAY("hxtpay"),
    /// 好生意(占位通道, 未对接)
    HSY_PAY("haoshengyi"),
    /// 银盈通(占位通道, 未对接)
    YYT_PAY("yinyingtong"),
    /// 支付宝云支付(占位通道, 未对接)
    CLOUD_PAY("cloudpay"),
    /// O米支付(占位通道, 未对接)
    OMI_PAY("omipay"),
    /// 慧收钱(占位通道, 未对接)
    HSQ_PAY("huishouqian"),
    /// 收银呗(占位通道, 未对接)
    SYB_PAY("shouyinbei"),
    /// 金多多(占位通道, 未对接)
    JINDD_PAY("jindd"),
    /// 鸿闻Pay(占位通道, 未对接)
    HWKJ_PAY("hwkpay"),
    /// Payssion(占位通道, 未对接)
    PAYSSION("payssion"),
    /// 超级支付(占位通道, 未对接)
    PAYPRO("paypro"),
    /// 商福通(占位通道, 未对接)
    SHANGFUTONG_PAY("shangfutong"),
    /// 智付(占位通道, 未对接)
    DINPAY("dinpay"),
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
