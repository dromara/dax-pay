package org.dromara.daxpay.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 进件类型
 * 字典:
 * @author xxm
 * @since 2025/1/5
 */
@Getter
@AllArgsConstructor
public enum IsvApplyTypeEnum {
    FACE_TO_FACE("face_to_face", "当面付"),
    APP("app", "APP"),
    OTHER("other", "其他"),
    MERCHANT("merchant", "商户进件"),
    ;

    private final String code;
    private final String name;
}
