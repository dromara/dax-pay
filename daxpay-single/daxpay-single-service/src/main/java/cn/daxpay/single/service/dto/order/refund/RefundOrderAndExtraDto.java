package cn.daxpay.single.service.dto.order.refund;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 退款订单信息
 * @author xxm
 * @since 2024/4/28
 */
@Data
@Accessors(chain = true)
@Schema(title = "退款订单信息")
public class RefundOrderAndExtraDto {
    @Schema(description = "退款订单")
    RefundOrderDto refundOrder;
    @Schema(description = "退款订单扩展信息")
    RefundOrderExtraDto refundOrderExtra;
}
