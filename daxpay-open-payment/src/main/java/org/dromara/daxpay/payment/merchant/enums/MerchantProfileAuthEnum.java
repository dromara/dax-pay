package org.dromara.daxpay.payment.merchant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 商户主体资料认证状态
 * 字典: merchant_profile_auth
 * @author xxm
 * @since 2025/9/19
 */
@Getter
@AllArgsConstructor
public enum MerchantProfileAuthEnum {
    /** 未认证/待认证/成功/认证失败 */
    UNAUTHENTICATED("unauthenticated", "未认证"),
    WAITING("waiting", "待认证"),
    SUCCESS("success", "认证成功"),
    FAILED("failed", "认证失败");

    private final String code;
    private final String name;

}
