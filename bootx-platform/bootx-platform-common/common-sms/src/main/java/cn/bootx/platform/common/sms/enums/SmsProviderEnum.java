package cn.bootx.platform.common.sms.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 短信供应商枚举
 * @author xxm
 * @since 2025/10/13
 */
@Getter
@AllArgsConstructor
public enum SmsProviderEnum {
    ALIYUN("aliyun","阿里云"),
    TENCENT("tencent","腾讯云"),
    ;

    private final String code;
    private final String name;
}
