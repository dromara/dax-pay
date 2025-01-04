package org.dromara.daxpay.core.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 支付通道枚举
 * 字典值: channel
 *
 * @author xxm
 * @since 2021/7/26
 */
@Getter
@RequiredArgsConstructor
public enum ChannelEnum {

    /** 支付宝 - 直连商户 */
    ALIPAY("ali_pay"),
    /** 支付宝 - 服务商 */
    ALIPAY_ISV("alipay_isv"),
    /** 微信支付 */
    WECHAT("wechat_pay"),
    /** 微信支付服务商 */
    WECHAT_ISV("wechat_pay_isv"),
    /** 云闪付 */
    UNION_PAY("union_pay"),

    ;

    /** 支付通道编码 */
    private final String code;
}
