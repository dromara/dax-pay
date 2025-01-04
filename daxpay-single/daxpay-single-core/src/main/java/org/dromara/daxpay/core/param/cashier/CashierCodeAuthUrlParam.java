package org.dromara.daxpay.core.param.cashier;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 获取收银码牌认证url参数
 * @author xxm
 * @since 2024/9/28
 */
@Data
@Accessors(chain = true)
@Schema(title = "收银码牌认证参数")
public class CashierCodeAuthUrlParam {

    @Schema(description = "收银台code")
    private String cashierCode;

    @Schema(description = "收银码牌类型")
    private String cashierType;
}
