package cn.daxpay.single.sdk.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 支付通道枚举
 *
 * @author xxm
 * @since 2021/7/26
 */
@Getter
@RequiredArgsConstructor
public enum ChannelEnum {

    /** 支付宝 - 直连商户 */
    ALI("ali_pay"),
    /** 微信支付 */
    WECHAT("wechat_pay"),
    /** 云闪付 */
    UNION_PAY("union_pay"),
    ;
    /** 支付通道编码 */
    private final String code;

}
