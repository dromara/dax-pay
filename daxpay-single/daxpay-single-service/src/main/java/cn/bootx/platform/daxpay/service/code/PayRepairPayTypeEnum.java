package cn.bootx.platform.daxpay.service.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付修复时的支付/退款类型
 * @author xxm
 * @since 2024/1/28
 */
@Getter
@AllArgsConstructor
public enum PayRepairPayTypeEnum {

    PAY("pay","支付"),
    REFUND("refund","退款");

    private final String code;
    private final String name;
}
