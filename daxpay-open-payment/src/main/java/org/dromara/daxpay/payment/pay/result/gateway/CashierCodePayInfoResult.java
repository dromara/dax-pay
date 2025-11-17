package org.dromara.daxpay.payment.pay.result.gateway;

import org.dromara.daxpay.payment.unipay.enums.GatewayCallTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 码牌支付信息
 * @author xxm
 * @since 2025/10/11
 */
@Data
@Accessors(chain = true)
@Schema(title = "码牌支付信息")
public class CashierCodePayInfoResult {

    /** 金额类型 固定金额/任意金额 */
    private String amountType;

    /** 金额 */
    private BigDecimal amount;

    /** 码牌名称 */
    private String name;

    /** 编号 */
    private String code;

    /**
     * 调用方式
     * @see GatewayCallTypeEnum
     */
    private String callType;

    /** 需要OpenId认证 */
    private boolean needOpenId;


}
