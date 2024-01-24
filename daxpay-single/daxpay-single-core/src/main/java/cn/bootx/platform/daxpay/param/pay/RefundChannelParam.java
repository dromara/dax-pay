package cn.bootx.platform.daxpay.param.pay;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 分通道退款参数
 * @author xxm
 * @since 2023/12/18
 */
@Data
@Accessors(chain = true)
@Schema(title = "分通道退款参数")
public class RefundChannelParam {

    /**
     * @see PayChannelEnum#getCode()
     */
    @Schema(description = "支付通道编码")
    @NotBlank(message = "支付通道编码不可为空")
    private String channel;

    @Schema(description = "退款金额")
    @NotNull(message = "退款金额不可为空")
    @Min(1)
    private Integer amount;

}
