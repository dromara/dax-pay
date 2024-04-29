package cn.bootx.platform.daxpay.service.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付网关接口支持回调类型的枚举
 * @author xxm
 * @since 2024/2/10
 */
@Getter
@AllArgsConstructor
public enum PayApiCallBackTypeEnum {

    /** 支持所有回调类型 */
    ALL("all"),
    /** 不支持退掉通知 */
    NONE("none");

    private final String code;
}
