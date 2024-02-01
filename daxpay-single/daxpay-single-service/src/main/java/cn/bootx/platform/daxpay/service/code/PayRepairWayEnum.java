package cn.bootx.platform.daxpay.service.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付修复方式
 * @author xxm
 * @since 2023/12/28
 */
@Getter
@AllArgsConstructor
public enum PayRepairWayEnum {

    SUCCESS("pay_success","支付成功"),
    CLOSE_LOCAL("pay_close_local","关闭本地支付"),
    PROGRESS("pay_progress","切换到支付中"),
    /** 同时也会关闭本地支付 */
    CLOSE_GATEWAY("pay_close_gateway","关闭网关支付");

    private final String code;
    private final String name;
}
