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

    /** 支付宝 - 官方商户 */
    ALIPAY("ali_pay"),
    /** 支付宝 - 服务商 */
    ALIPAY_ISV("alipay_isv"),
    /** 微信支付 - 官方商户 */
    WECHAT("wechat_pay"),
    /** 微信支付服务商 */
    WECHAT_ISV("wechat_pay_isv"),
    /** 云闪付 */
    UNION_PAY("union_pay"),
    /** 乐刷 */
    LESHUA_PAY("leshua_pay"),
    /** 随行付 */
    VBILL_PAY("vbill_pay"),
    /** 汇付天下AdaPay */
    ADA_PAY("ada_pay"),
    /** 通联支付 */
    ALLIN_PAY("allin_pay"),
    /** 拉卡拉支付 */
    LAKALA_PAY("lakala_pay"),
    /** 富友支付 */
    FU_YOU("fu_you"),
    /** 盛付通 */
    SHENG_PAY("sheng_pay"),
    /** 银盛 */
    YSEP_PAY("yse_pay"),
    ;

    /** 支付通道编码 */
    private final String code;
}
