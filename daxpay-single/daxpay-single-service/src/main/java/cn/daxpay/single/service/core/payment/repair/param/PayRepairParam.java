package cn.daxpay.single.service.core.payment.repair.param;

import cn.daxpay.single.service.code.PayAdjustWayEnum;
import cn.daxpay.single.service.code.TradeAdjustSourceEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 支付订单修复参数
 * @author xxm
 * @since 2023/12/27
 */
@Data
@Accessors(chain = true)
public class PayRepairParam {

    @Schema(description = "触发来源")
    private TradeAdjustSourceEnum source;

    @Schema(description = "调整方式")
    private PayAdjustWayEnum adjustWay;

    @Schema(description = "支付订单号")
    private String orderNo;
}
