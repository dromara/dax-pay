package cn.bootx.platform.daxpay.result.pay;

import cn.bootx.platform.daxpay.result.CommonResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 退款响应参数
 * @author xxm
 * @since 2023/12/18
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "退款响应参数")
public class RefundResult extends CommonResult {

    @Schema(description = "退款ID")
    private Long refundId;

    @Schema(description = "退款订单号, 如果请求时未传, 则默认使用退款ID")
    private String refundNo;

    @Schema(description = "退款状态")
    private String status;
}
