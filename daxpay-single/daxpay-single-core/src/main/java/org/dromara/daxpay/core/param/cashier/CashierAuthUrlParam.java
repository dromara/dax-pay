package org.dromara.daxpay.core.param.cashier;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 获取通道收银认证url参数
 * @author xxm
 * @since 2024/9/28
 */
@Data
@Accessors(chain = true)
@Schema(title = "通道收银认证参数")
public class CashierAuthUrlParam {

    @Schema(description = "应用号")
    private String appId;

    @Schema(description = "收银台类型")
    private String cashierType;
}
