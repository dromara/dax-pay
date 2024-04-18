package cn.bootx.platform.daxpay.service.dto.order.allocation;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import cn.bootx.platform.daxpay.code.AllocationOrderResultEnum;
import cn.bootx.platform.daxpay.code.AllocationOrderStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 分账订单
 * @author xxm
 * @since 2024/4/7
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "分账订单")
public class AllocationOrderDto extends BaseDto {

    /**
     * 分账订单号
     */
    @Schema(description = "分账订单号")
    private String orderNo;

    /**
     * 支付订单ID
     */
    @Schema(description = "支付订单ID")
    private Long paymentId;

    /**
     * 支付订单标题
     */
    @Schema(description = "支付订单标题")
    private String title;

    /**
     * 网关支付订单号
     */
    @Schema(description = "网关支付订单号")
    private String gatewayPayOrderNo;

    /**
     * 网关分账单号
     */
    @Schema(description = "网关分账单号")
    private String gatewayAllocationNo;

    /**
     * 分账单号
     */
    @Schema(description = "分账单号")
    private String allocationNo;

    /**
     * 所属通道
     */
    @Schema(description = "所属通道")
    private String channel;

    /**
     * 总分账金额
     */
    @Schema(description = "总分账金额")
    private Integer amount;

    /**
     * 分账描述
     */
    @Schema(description = "分账描述")
    private String description;

    /**
     * 状态
     * @see AllocationOrderStatusEnum
     */
    @Schema(description = "状态")
    private String status;

    /**
     * 分账处理结果
     * @see AllocationOrderResultEnum
     */
    @Schema(description = "分账处理结果")
    private String result;
    /**
     * 错误原因
     */
    @Schema(description = "错误原因")
    private String errorMsg;

}
