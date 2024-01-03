package cn.bootx.platform.daxpay.service.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付修复类型
 * @author xxm
 * @since 2023/12/28
 */
@Getter
@AllArgsConstructor
public enum PayRepairTypeEnum {

    SUCCESS("success","成功"),
    CLOSE("close","关闭"),
    TIMEOUT("timeout","超时关闭"),
    REFUND("refund","退款");

    private final String code;
    private final String name;
}
