package cn.bootx.platform.daxpay.result.pay;

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

    @Schema(description = "退款ID")
    private Long refundId;

    @Schema(description = "退款订单号")
    private String refundNo;

    @Schema(description = "退款状态")
    private String state;

}
