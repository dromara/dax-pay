package cn.bootx.platform.daxpay.service.core.payment.notice.result;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.PayWayEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 各支付通道参数
 * @author xxm
 * @since 2024/1/7
 */
@Data
@Accessors(chain = true)
@Schema(title = "各支付通道参数")
public class PayChannelResult {
    /**
     * @see PayChannelEnum#getCode()
     */
    @Schema(description = "支付通道编码")
    private String channel;

    /**
     * @see PayWayEnum#getCode()
     */
    @Schema(description = "支付方式编码")
    private String way;

    @Schema(description = "支付金额")
    private Integer amount;

}
