package org.dromara.daxpay.channel.alipay.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 签约类型
 * @author xxm
 * @since 2024/11/5
 */
@Getter
@AllArgsConstructor
public enum AlipayApplyTypeEnum {
    APP("app", "APP"),
    FACE_TO_FACE("face_to_face","当面付"),
    OTHER("other","其他");

    private final String code;
    private final String name;
}
