package cn.bootx.platform.daxpay.service.param.reconcile;

import cn.bootx.platform.common.core.annotation.QueryParam;
import cn.bootx.platform.daxpay.code.PayReconcileTradeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 对账详情查询
 * @author xxm
 * @since 2024/1/22
 */
@QueryParam(type = QueryParam.CompareTypeEnum.EQ)
@Data
@Accessors(chain = true)
@Schema(title = "对账详情查询")
public class ReconcileDetailQuery {

    @Schema(description = "关联对账订单ID")
    private Long recordOrderId;

    /**
     * 交易类型
     * @see PayReconcileTradeEnum
     */
    @Schema(description = "交易类型")
    private String type;

    /** 本地支付ID */
    @Schema(description = "本地支付ID")
    private String paymentId;

    /** 本地退款ID */
    @Schema(description = "本地退款ID")
    private String refundId;

    /** 网关订单号 - 支付宝/微信的订单号 */
    @Schema(description = "网关订单号")
    private String gatewayOrderNo;

    /** 交易金额 */
    @Schema(description = "交易金额")
    private Integer amount;

    /** 订单名称 */
    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    @Schema(description = "订单名称")
    private String title;
}
