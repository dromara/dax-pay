package cn.bootx.platform.daxpay.service.param.order;

import cn.bootx.platform.common.core.annotation.QueryParam;
import cn.bootx.platform.common.core.rest.param.QueryOrder;
import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.RefundStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 支付退款查询参数
 * @author xxm
 * @since 2024/1/9
 */
@EqualsAndHashCode(callSuper = true)
@Data
@QueryParam(type = QueryParam.CompareTypeEnum.EQ)
@Accessors(chain = true)
@Schema(title = "支付退款查询参数")
public class RefundOrderQuery extends QueryOrder {

    @Schema(description = "退款号")
    private String refundNo;

    @Schema(description = "商户退款号")
    private String bizRefundNo;

    /** 三方支付系统退款交易号 */
    @Schema(description = "三方支付系统退款交易号")
    private String outRefundNo;

    /** 支付订单ID */
    @Schema(description = "支付订单ID")
    private Long orderId;

    /** 支付订单号 */
    @Schema(description = "支付订单号")
    private String orderNo;

    /** 商户支付订单号 */
    @Schema(description = "商户支付订单号")
    private String bizOrderNo;

    /** 支付标题 */
    @Schema(description = "支付标题")
    private String title;

    /**
     * 退款通道
     * @see PayChannelEnum
     */
    @Schema(description = "支付通道")
    private String channel;

    /**
     * @see RefundStatusEnum
     */
    @Schema(description = "退款状态")
    private String status;

    /** 错误码 */
    @Schema(description = "错误码")
    private String errorCode;
}
