package cn.daxpay.single.demo.param;

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

    @Schema(description = "商户订单号")
    @NotNull(message = "商户订单号不能为空")
    private String bizOrderNo;

    @Schema(description = "是否分账")
    private Boolean allocation;


    @Schema(description = "标题")
    @NotNull(message = "标题不能为空")
    private String title;

    @Schema(description = "金额")
    @NotNull(message = "金额不能为空")
    private BigDecimal amount;

    @Schema(description = "openId(微信支付时使用)")
    private String openId;

    @Schema(description = "支付通道")
    @NotNull(message = "支付通道不能为空")
    private String channel;

    @Schema(description = "支付方式")
    @NotNull(message = "支付方式不能为空")
    private String method;

    @Schema(description = "付款码")
    private String authCode;

}
