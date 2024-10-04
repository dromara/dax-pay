package org.dromara.daxpay.core.param.cashier;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 获取通道收银认证结果参数
 * @author xxm
 * @since 2024/9/28
 */
@Data
@Accessors(chain = true)
@Schema(title = "通道收银认证参数")
public class CashierAuthCodeParam {

    @Schema(description = "应用号")
    private String appId;

    @Schema(description = "收银台类型")
    private String cashierType;

    @Schema(description = "认证Code")
    private String authCode;
}
