package cn.bootx.platform.daxpay.service.dto.order.pay;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 支付订单和扩展信息
 * @author xxm
 * @since 2024/4/26
 */
@Data
@Schema(title = "支付订单和扩展信息")
public class PayOrderDetailDto {
    @Schema(description = "支付订单")
    private PayOrderDto payOrder;
    @Schema(description = "支付订单扩展信息")
    private PayOrderExtraDto payOrderExtra;
}
