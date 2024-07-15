package cn.daxpay.single.service.core.payment.adjust.param;

import cn.daxpay.single.service.code.RefundAdjustWayEnum;
import cn.daxpay.single.service.code.TradeAdjustSourceEnum;
import cn.daxpay.single.service.core.order.refund.entity.RefundOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 退款订单修复参数
 * @author xxm
 * @since 2023/12/27
 */
@Data
@Accessors(chain = true)
public class RefundAdjustParam {

    @Schema(description = "退款订单")
    private RefundOrder order;

    @Schema(description = "通道交易号")
    private String outTradeNo;

    @Schema(description = "完成时间")
    private LocalDateTime finishTime;

    @Schema(description = "触发来源")
    private TradeAdjustSourceEnum source;

    @Schema(description = "调整方式")
    private RefundAdjustWayEnum adjustWay;
}
