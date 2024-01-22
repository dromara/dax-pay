package cn.bootx.platform.daxpay.service.dto.order.reconcile;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 对账订单详情
 * @author xxm
 * @since 2024/1/22
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "对账订单详情")
public class PayReconcileDetailDto extends BaseDto {

    @Schema(description = "关联对账订单ID")
    private Long recordOrderId;

    /**
     * @see cn.bootx.platform.daxpay.code.PayStatusEnum
     */
    @Schema(description = "交易状态")
    private String status;

    /** 交易类型 支付/退款 */
    @Schema(description = "交易类型")
    private String type;

    /** 订单id - 支付ID/退款ID等 */
    @Schema(description = "订单id")
    private String orderId;

    /** 网关订单号 - 支付宝/微信的订单号 */
    @Schema(description = "网关订单号")
    private String gatewayOrderNo;

    /** 交易金额 */
    @Schema(description = "交易金额")
    private Integer amount;

    /** 商品名称 */
    @Schema(description = "商品名称")
    private String title;
}
