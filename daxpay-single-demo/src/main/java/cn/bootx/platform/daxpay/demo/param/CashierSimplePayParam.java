package cn.bootx.platform.daxpay.demo.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 结算台简单支付参数(单通道支付)
 *
 * @author xxm
 * @since 2022/2/23
 */
@Data
@Accessors(chain = true)
@Schema(title = "结算台简单支付参数(单通道支付)")
public class CashierSimplePayParam {

    @Schema(description = "业务号")
    @NotNull
    private String businessNo;

    @Schema(description = "是否分账")
    private boolean allocation;

    @Schema(description = "标题")
    @NotNull
    private String title;

    @Schema(description = "金额")
    @NotNull
    private BigDecimal amount;

    @Schema(description = "openId(微信支付时使用)")
    private String openId;

    @Schema(description = "支付渠道")
    @NotNull
    private String channel;

    @Schema(description = "支付方式")
    @NotNull
    private String payWay;

    @Schema(description = "付款码")
    private String authCode;

}
