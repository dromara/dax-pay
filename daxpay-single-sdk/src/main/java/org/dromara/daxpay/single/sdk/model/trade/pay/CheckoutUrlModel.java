package org.dromara.daxpay.single.sdk.model.trade.pay;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 收银台响应参数
 * @author xxm
 * @since 2024/12/3
 */
@Data
public class CheckoutUrlModel {
    @Schema(description = "收银台链接")
    private String url;

}
