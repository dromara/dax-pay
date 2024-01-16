package cn.bootx.platform.daxpay.param.pay;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 查询退款订单参数类
 * @author xxm
 * @since 2024/1/16
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "查询退款订单参数类")
public class QueryRefundParam extends PayCommonParam{

    @Schema(description = "退款订单ID")
    private Long refundId;

    @Schema(description = "退款订单号")
    private String refundNo;
}
