package cn.bootx.platform.daxpay.service.param.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 支付退款查询参数
 * @author xxm
 * @since 2024/1/9
 */
@Data
@Accessors(chain = true)
@Schema(title = "支付退款查询参数")
public class PayRefundOrderQuery {

    @Schema(description = "关联的业务号")
    private String businessNo;

    @Schema(description = "付款付单号")
    private Long paymentId;

    @Schema(description = "异步方式关联退款请求号(部分退款情况)")
    private String refundRequestNo;

    @Schema(description = "标题")
    private String title;
}
