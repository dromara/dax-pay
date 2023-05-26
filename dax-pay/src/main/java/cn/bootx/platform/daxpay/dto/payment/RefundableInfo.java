package cn.bootx.platform.daxpay.dto.payment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 可退款信息
 *
 * @author xxm
 * @date 2022/3/2
 */
@Data
@Accessors(chain = true)
@Schema(title = "可退款信息")
public class RefundableInfo {

    @Schema(description = "支付通道")
    private String payChannel;

    @Schema(description = "金额")
    private BigDecimal amount;

}
