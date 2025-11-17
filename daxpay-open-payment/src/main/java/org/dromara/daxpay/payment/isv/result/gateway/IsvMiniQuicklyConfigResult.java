package org.dromara.daxpay.payment.isv.result.gateway;

import org.dromara.daxpay.payment.merchant.result.info.MchResult;
import org.dromara.daxpay.payment.unipay.enums.PayLimitPayEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 小程序快捷支付配置结果
 * @author xxm
 * @since 2025/10/10
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "小程序快捷支付配置结果")
public class IsvMiniQuicklyConfigResult extends MchResult {

    /**
     * 限制用户支付类型, 目前支持限制信用卡
     * @see PayLimitPayEnum
     */
    @Schema(description = "限制用户支付类型")
    private String limitPay;

    /** 服务商号 */
    @Schema(description = "服务商号")
    private String isvNo;

}
