package org.dromara.daxpay.core.param.gateway;

import org.dromara.daxpay.core.enums.CashierSceneEnum;
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

    @Schema(description = "码牌code")
    private String cashierCode;

    @Schema(description = "支付金额")
    private BigDecimal amount;

    /**
     * 支付场景
     * @see CashierSceneEnum
     */
    @Schema(description = "支付场景")
    private String cashierScene;

    @Schema(description = "唯一标识")
    private String openId;

    @Schema(description = "描述/备注")
    private String description;

}
