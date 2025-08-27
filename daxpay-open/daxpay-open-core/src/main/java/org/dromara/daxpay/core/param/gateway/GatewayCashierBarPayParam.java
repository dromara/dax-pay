package org.dromara.daxpay.core.param.gateway;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 收银台聚合付款码支付
 * @author xxm
 * @since 2025/4/2
 */
@Data
@Accessors(chain = true)
@Schema(title = "收银台聚合付款码支付")
public class GatewayCashierBarPayParam {

    @Schema(description = "要支付的订单号")
    private String orderNo;

    @Schema(description = "付款码")
    private String authCode;
}
