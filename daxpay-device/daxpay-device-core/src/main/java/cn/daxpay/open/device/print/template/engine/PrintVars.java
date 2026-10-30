package cn.daxpay.open.device.print.template.engine;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/// # 打印模板变量池
///
/// 模板占位符 `${xxx}` 的白名单(封闭集合, 未知占位符在解析校验时报错而非静默空串)。
/// 渠道名等派生变量由推送链路组装时换算(见 [asChannelName]), 模板侧只消费最终字符串。
///
/// 移植自 plus `daxpay-device-core`(快照 773134af): 开源版无通道枚举中文名直查
/// (plus [ChannelEnum] 带 name 字段, 开源版枚举走 i18n key), 渠道中文名收敛为内置
/// 五键映射表, 未登记通道统一"扫码收款"(与终端 APP 词元表兜底口径一致)。
public final class PrintVars {

    /// 商户名称
    public static final String MCH_NAME = "mchName";

    /// 关联应用名称(未绑为空串)
    public static final String APP_NAME = "appName";

    /// 打印设备名称
    public static final String DEVICE_NAME = "deviceName";

    /// 设备编号(设备台账号; 自检票二维码承载此值, 便于扫回定位是哪台机器)
    public static final String DEVICE_NO = "deviceNo";

    /// 渠道中文名(微信/支付宝/抖音支付/银联/扫码收款)
    public static final String CHANNEL_NAME = "channelName";

    /// 金额(分)
    public static final String AMOUNT_FEN = "amountFen";

    /// 金额(元, 两位小数)
    public static final String AMOUNT_YUAN = "amountYuan";

    /// 平台支付单号
    public static final String BIZ_NO = "bizNo";

    /// 商户订单号
    public static final String OUT_BIZ_NO = "outBizNo";

    /// 支付时间(yyyy-MM-dd HH:mm:ss)
    public static final String PAID_TIME = "paidTime";

    /// 门店名称(支付单未落门店时为空串; 连锁店小票头靠本变量区分, 不做门店级模板配置)
    public static final String STORE_NAME = "storeName";

    /// 商品合计(元, 两位小数; 无明细为空串)
    public static final String GOODS_TOTAL = "goodsTotal";

    /// 商品差额(实付 − 商品合计, 带符号; 无明细或差额为 0 为空串)
    public static final String GOODS_DIFF = "goodsDiff";

    /// 变量名 → 中文说明(顺序即展示顺序)
    public static final Map<String, String> ALL;

    static {
        LinkedHashMap<String, String> m = new LinkedHashMap<>();
        m.put(MCH_NAME, "商户名称");
        m.put(APP_NAME, "关联应用名称");
        m.put(DEVICE_NAME, "设备名称");
        m.put(DEVICE_NO, "设备编号");
        m.put(CHANNEL_NAME, "渠道中文名");
        m.put(AMOUNT_FEN, "金额(分)");
        m.put(AMOUNT_YUAN, "金额(元)");
        m.put(BIZ_NO, "平台支付单号");
        m.put(OUT_BIZ_NO, "商户订单号");
        m.put(PAID_TIME, "支付时间");
        m.put(STORE_NAME, "门店名称");
        m.put(GOODS_TOTAL, "商品合计(元)");
        m.put(GOODS_DIFF, "商品差额(实付−合计)");
        ALL = java.util.Collections.unmodifiableMap(m);
    }

    /// 变量名集合(白名单校验用)
    public static final Set<String> NAMES = ALL.keySet();

    private PrintVars() {
    }

    /// 渠道 code → 中文名(与虚拟设备 APP 语音前缀口径一致, 未知归"扫码收款")
    public static String asChannelName(String channel) {
        if (channel == null) {
            return "扫码收款";
        }
        return switch (channel.trim().toLowerCase()) {
            case "wechat", "wx", "微信" -> "微信";
            case "alipay", "ali", "支付宝" -> "支付宝";
            case "douyin", "tt", "抖音" -> "抖音支付";
            case "union", "union_pay", "银联" -> "银联";
            // 其余通道(乐刷/拉卡拉/银联商务等)开源版无通道中文名字典, 统一"扫码收款"
            // (待核: 如需通道实名展示, 后续接 i18n 通道枚举词条)
            default -> "扫码收款";
        };
    }

    /// 内置样例数据(预览闭环用, 单号刻意取 ASCII 供条码示例)
    public static Map<String, String> sample() {
        Map<String, String> vars = new LinkedHashMap<>();
        vars.put(MCH_NAME, "示例商户");
        vars.put(APP_NAME, "聚合收银");
        vars.put(STORE_NAME, "示例门店");
        vars.put(DEVICE_NAME, "前台一号机");
        vars.put(DEVICE_NO, "VD202610200001");
        vars.put(CHANNEL_NAME, asChannelName("wechat"));
        vars.put(AMOUNT_FEN, "1250");
        vars.put(AMOUNT_YUAN, "12.50");
        vars.put(BIZ_NO, "P202610201234567890");
        vars.put(OUT_BIZ_NO, "ORD20261020001");
        vars.put(PAID_TIME, "2026-10-20 12:00:34");
        // 商品派生变量: 样例明细合计 13.50, 实付 12.50, 差额 -1.00(演示优惠行; 实际渲染由 PrintTemplateRenderer 重算覆盖)
        vars.put(GOODS_TOTAL, asAmountYuan(1350L));
        vars.put(GOODS_DIFF, "-1.00");
        return vars;
    }

    /// 分 → 元两位小数(推送链路组装 amountYuan 用)
    public static String asAmountYuan(long amountFen) {
        return String.format("%.2f", amountFen / 100.0);
    }
}
