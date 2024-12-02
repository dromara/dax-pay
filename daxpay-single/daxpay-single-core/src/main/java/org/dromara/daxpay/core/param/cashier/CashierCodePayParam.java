package org.dromara.daxpay.core.param.cashier;

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
public class CashierCodePayParam {

    @Schema(description = "收银码牌Code")
    private String cashierCode;

    @Schema(description = "支付金额")
    private BigDecimal amount;

    @Schema(description = "收银台类型")
    private String cashierType;

    @Schema(description = "唯一标识")
    private String openId;

    @Schema(description = "描述")
    private String description;
}
