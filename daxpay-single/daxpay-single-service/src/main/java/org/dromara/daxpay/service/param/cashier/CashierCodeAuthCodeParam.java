package org.dromara.daxpay.service.param.cashier;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 收银码牌认证码参数
 * @author xxm
 * @since 2024/11/25
 */
@Data
@Accessors(chain = true)
@Schema(title = "收银码牌认证码参数")
public class CashierCodeAuthCodeParam {

    @Schema(description = "码牌Code")
    private String code;

    @Schema(description = "收银台类型")
    private String type;

    @Schema(description = "认证Code")
    private String authCode;
}
