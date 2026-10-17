package cn.daxpay.open.channel.fubei.client.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/// # 付呗支付方式(主应用侧)
///
/// 按参考插件(fubei)能力映射平台支付方式到付呗 fbpay.order.create / fbpay.order.wap.create 的组合参数:
/// - 微信 JSAPI → fbpay.order.create(pay_type=wxpay, user_id=openid, sub_appid)
/// - 支付宝 JSAPI → fbpay.order.create(pay_type=alipay, user_id=支付宝买家用户ID)
/// - 云闪付扫码 → fbpay.order.create(pay_type=unionpay, 不传 user_id, 返回 qr_code)
/// - 支付宝 H5 → fbpay.order.wap.create(独立接口, 返回自提交表单 HTML 或跳转 URL)
///
/// 枚举 name 与平台 [cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum] 对齐,
/// 由主应用映射后经 JSON 传输, 子应用直接按 name 反序列化。
///
/// 付呗微信/支付宝的 C扫B 台牌码与付款码被扫为门店台牌/收银机能力, 参考插件未走开放平台接口, 一期未接。
@Getter
@AllArgsConstructor
public enum FubeiPayMethod {

    /// 微信 JSAPI/小程序(openid + sub_appid → sign_package)
    WECHAT_JSAPI("wechat_jsapi", true),
    /// 支付宝 JSAPI(买家用户ID → prepay_id)
    ALIPAY_JSAPI("alipay_jsapi", true),
    /// 云闪付扫码(→ qr_code)
    UNION_QR("union_qr", false),
    /// 支付宝 H5(wap.create → html/跳转 URL)
    ALIPAY_H5("alipay_h5", false);

    /// 平台支付方式编码(对齐 PayMethodEnum.code)
    private final String code;

    /// 是否 JSAPI 族(下单必传 user_id: 微信=openid / 支付宝=买家用户ID)
    private final boolean needUserId;

    /// 根据平台支付方式编码查找, 未知返回 null
    public static FubeiPayMethod findByCodeOrNull(String code) {
        return Arrays.stream(values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                .orElse(null);
    }
}
