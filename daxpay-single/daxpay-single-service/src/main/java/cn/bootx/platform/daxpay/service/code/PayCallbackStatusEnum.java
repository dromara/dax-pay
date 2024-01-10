package cn.bootx.platform.daxpay.service.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付回调处理状态
 * @author xxm
 * @since 2023/12/20
 */
@Getter
@AllArgsConstructor
public enum PayCallbackStatusEnum {

    SUCCESS("success","成功"),
    FAIL("fail","失败"),
    IGNORE("ignore","忽略"),
    TIMEOUT("timeout","超时"),
    NOT_FOUND("not_found","未找到");

    private final String code;
    private final String name;
}
