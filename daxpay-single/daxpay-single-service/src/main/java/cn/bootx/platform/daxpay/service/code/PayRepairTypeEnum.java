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

    SUCCESS("success","支付成功"),
    CLOSE_LOCAL("close_local","关闭本地支付"),
    WAIT_PAY("wait_pay","待支付"),
    /** 同时也会关闭本地支付 */
    CLOSE_GATEWAY("close_gateway","关闭网关支付"),
    REFUND("refund","退款");

    private final String code;
    private final String name;
}
