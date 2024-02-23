package cn.bootx.platform.daxpay.service.core.payment.notice.result;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 退款通道信息
 * @author xxm
 * @since 2024/2/22
 */
@Data
@Accessors(chain = true)
@Schema(title = "退款通道信息")
public class RefundChannelResult {

    /**
     * @see PayChannelEnum#getCode()
     */
    @Schema(description = "通道编码")
    private String channel;

    @Schema(description = "退款金额")
    private Integer amount;
}
