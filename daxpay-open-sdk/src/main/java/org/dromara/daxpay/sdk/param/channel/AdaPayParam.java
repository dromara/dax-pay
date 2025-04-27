package org.dromara.daxpay.sdk.param.channel;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 汇付支付参数
 * @author xxm
 * @since 2025/3/4
 */
@Data
@Accessors(chain = true)
public class AdaPayParam {

    /** 买家的支付宝用户 id */
    private String buyerId;

    /** 买家支付宝账号 */
    private String buyerLogonId;

    /** openid */
    private String openId;
}
