package org.dromara.daxpay.service.param.order.allocation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 分账订单查询参数
 * @author xxm
 * @since 2024/4/7
 */
@Data
@Accessors(chain = true)
@Schema(title = "分账订单查询参数")
public class AllocOrderQuery {

    @Schema(description = "分账订单号")
    private String orderNo;

    @Schema(description = "支付订单ID")
    private Long paymentId;

    @Schema(description = "支付订单标题")
    private String title;

    @Schema(description = "分账业务号")
    private String allocNo;

    @Schema(description = "分账通道")
    private String channel;




}
