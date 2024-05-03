package cn.bootx.platform.daxpay.service.dto.order.reconcile;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import cn.bootx.platform.daxpay.service.code.PaymentTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 对账订单详情
 * @author xxm
 * @since 2024/1/22
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "对账订单详情")
public class ReconcileDetailDto extends BaseDto {

    /** 关联对账订单ID */
    @Schema(description = "关联对账订单ID")
    private Long reconcileId;

    /** 商品名称 */
    @Schema(description = "商品名称")
    private String title;

    /** 交易金额 */
    @Schema(description = "交易金额")
    private Integer amount;

    /**
     * 交易类型
     * @see PaymentTypeEnum
     */
    @Schema(description = "交易类型")
    private String type;

    /** 本地交易号 */
    @Schema(description = "本地交易号")
    private String tradeNo;

    /** 外部交易号 - 支付宝/微信的订单号 */
    @Schema(description = "外部交易号")
    private String outTradeNo;

    /** 交易时间 */
    @Schema(description = "交易时间")
    private LocalDateTime tradeTime;
}
