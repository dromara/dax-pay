package org.dromara.daxpay.channel.wechat.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 微信OpenId认证方式
 * @author xxm
 * @since 2025/4/24
 */
@Getter
@AllArgsConstructor
public enum WechatAuthTypeEnum {
    SP("sp","服务商用户标识"),
    SUB("sub","子商户应用用户标识"),
    ;
    private final String code;
    private final String name;
}
