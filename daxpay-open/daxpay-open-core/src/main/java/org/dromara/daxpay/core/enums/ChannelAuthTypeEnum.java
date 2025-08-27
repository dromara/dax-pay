package org.dromara.daxpay.core.enums;

import org.dromara.daxpay.core.exception.ConfigNotExistException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/**
 * 通道认证类型
 * 字典值: channel_auth_type
 * @author xxm
 * @since 2025/4/7
 */
@Getter
@AllArgsConstructor
public enum ChannelAuthTypeEnum {
    WECHAT("wechat", "微信"),
    ALIPAY("alipay", "支付宝"),
    UNION_PAY("union_pay", "银联"),
    ;

    private final String code;
    private final String name;

    public static ChannelAuthTypeEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(item -> Objects.equals(item.code, code))
                .findFirst()
                .orElseThrow(() -> new ConfigNotExistException("认证类型不存在"));
    }

}
