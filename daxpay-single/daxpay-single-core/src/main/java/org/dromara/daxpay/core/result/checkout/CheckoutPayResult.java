package org.dromara.daxpay.core.result.checkout;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.dromara.daxpay.core.enums.PayStatusEnum;

/**
 * 收银台支付结果
 * @author xxm
 * @since 2024/11/29
 */
@Data
@Accessors(chain = true)
@Schema(title = "收银台支付结果")
public class CheckoutPayResult {

    /** 链接 */
    private String url;

    /**
     * 支付状态
     * @see PayStatusEnum
     */
    @Schema(description = "支付结果")
    private String payStatus;
}
