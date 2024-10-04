package org.dromara.daxpay.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通道认证状态
 * @author xxm
 * @since 2024/9/24
 */
@Getter
@AllArgsConstructor
public enum ChannelAuthStatusEnum {

    /** 获取中 */
    WAITING("waiting"),
    /** 获取成功 */
    SUCCESS("success"),
    /** 数据不存在 */
    NOT_EXIST("not_exist");

    private final String code;

}
