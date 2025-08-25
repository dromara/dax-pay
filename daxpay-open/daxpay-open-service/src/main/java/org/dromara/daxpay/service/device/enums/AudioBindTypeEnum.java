package org.dromara.daxpay.service.device.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 音箱设备绑定类型枚举
 * 字典: audio_bind_type
 * @author xxm
 * @since 2025/7/8
 */
@Getter
@AllArgsConstructor
public enum AudioBindTypeEnum {

    APP("app", "应用"),
    MERCHANT("merchant", "商户"),
    CASHIER_CODE("cashier_code", "码牌"),
;
    private final String code;
    private final String name;
}
