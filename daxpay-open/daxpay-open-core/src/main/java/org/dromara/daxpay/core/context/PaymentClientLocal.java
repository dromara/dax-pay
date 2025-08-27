package org.dromara.daxpay.core.context;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 支付请求相关信息
 * @author xxm
 * @since 2023/12/25
 */
@Data
@Accessors(chain = true)
public class PaymentClientLocal {

    /** 客户端IP */
    private String clientIp;

    /** 请求IP */
    private String requestIp;

}
