package org.dromara.daxpay.core.result.trade.refund;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 退款响应参数
 * @author xxm
 * @since 2023/12/18
 */
@Data
@Accessors(chain = true)
@Schema(title = "退款响应参数")
public class RefundResult {

    /** 退款号 */
    @Schema(description = "退款号")
    private String refundNo;

    /** 商户退款号 */
    @Schema(description = "商户退款号")
    private String bizRefundNo;

    /** 退款状态 */
    @Schema(description = "退款状态")
    private String status;

    @Schema(description = "错误提示")
    private String errorMsg;
}
