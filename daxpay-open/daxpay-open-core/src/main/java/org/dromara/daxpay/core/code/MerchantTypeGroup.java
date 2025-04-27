package org.dromara.daxpay.core.code;

/**
 * 商户类型. 用于分组校验
 * @author xxm
 * @since 2024/11/4
 */
public interface MerchantTypeGroup {

    /** 普通商户 */
    interface common{}
    /** 特约商户 */
    interface partner{}
}
