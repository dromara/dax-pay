package org.dromara.daxpay.service.param.cashier;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 获取收银码牌认证url参数
 * @author xxm
 * @since 2024/11/25
 */
@Data
@Accessors(chain = true)
@Schema(title = "获取收银码牌认证url参数")
public class CashierCodeAuthUrlParam {

    @Schema(description = "码牌Code")
    private String code;

    @Schema(description = "收银台类型")
    private String type;
}
