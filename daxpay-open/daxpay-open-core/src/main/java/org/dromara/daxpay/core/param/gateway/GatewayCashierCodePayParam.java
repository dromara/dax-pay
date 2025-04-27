package org.dromara.daxpay.core.param.gateway;

import org.dromara.daxpay.core.enums.CashierCodeTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 收银码牌支付参数
 * @author xxm
 * @since 2024/9/28
 */
@Data
@Accessors(chain = true)
@Schema(title = "收银码牌支付参数")
public class GatewayCashierCodePayParam {

    @Schema(description = "码牌明细ID")
    private String cashierCode;

    @Schema(description = "支付金额")
    private BigDecimal amount;

    /**
     * 码牌类型
     * @see CashierCodeTypeEnum
     */
    @Schema(description = "码牌类型")
    private String cashierType;

    @Schema(description = "唯一标识")
    private String openId;

    @Schema(description = "描述/备注")
    private String description;

}
