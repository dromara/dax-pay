package cn.bootx.platform.daxpay.service.dto.order.refund;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import cn.bootx.platform.daxpay.code.RefundStatusEnum;
import cn.bootx.table.modify.annotation.DbColumn;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 支付退款通道订单
 * @author xxm
 * @since 2024/1/17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "支付退款通道订单")
public class RefundChannelOrderDto extends BaseDto {

    @DbColumn(comment = "关联退款id")
    @Schema(description = "关联退款id")
    private Long refundId;

    @Schema(description = "通道")
    private String channel;

    @Schema(description = "通道支付单id")
    private Long payChannelId;

    @Schema(description = "异步支付方式")
    private boolean async;

    @Schema(description = "订单金额")
    private Integer orderAmount;

    @Schema(description = "退款金额")
    private Integer amount;

    @Schema(description = "剩余可退余额")
    private Integer refundableAmount;

    /**
     * 退款状态
     * @see RefundStatusEnum
     */
    @Schema(description = "退款状态")
    private String status;
}
