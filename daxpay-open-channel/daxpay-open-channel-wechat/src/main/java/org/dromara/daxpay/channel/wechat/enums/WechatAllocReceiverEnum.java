package org.dromara.daxpay.channel.wechat.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 微信分账接收者类型
 * @author xxm
 * @since 2024/12/17
 */
@Getter
@AllArgsConstructor
public enum WechatAllocReceiverEnum {

    MERCHANT_ID("MERCHANT_ID", "商户号"),
    PERSONAL_OPENID("PERSONAL_OPENID", "openId"),
    ;

    private final String code;
    private final String name;
}
