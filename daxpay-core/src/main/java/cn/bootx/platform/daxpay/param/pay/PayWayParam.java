package cn.bootx.platform.daxpay.param.pay;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.PayWayEnum;
import cn.bootx.platform.daxpay.param.channel.AliPayParam;
import cn.bootx.platform.daxpay.param.channel.VoucherPayParam;
import cn.bootx.platform.daxpay.param.channel.WalletPayParam;
import cn.bootx.platform.daxpay.param.channel.WeChatPayParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 不只是支付, 退款发起时也是这个参数
 *
 * @author xxm
 * @since 2020/12/8
 */
@Data
@Accessors(chain = true)
@Schema(title = "支付方式参数")
public class PayWayParam implements Serializable {

    private static final long serialVersionUID = -46959864485463681L;

    /**
     * @see PayChannelEnum#getCode()
     */
    @Schema(description = "支付渠道编码")
    @NotBlank(message = "支付渠道编码不可为空")
    private String payChannel;

    /**
     * @see PayWayEnum#getCode()
     */
    @Schema(description = "支付方式编码")
    @NotBlank(message = "支付方式编码不可为空")
    private String payWay;

    @Schema(description = "支付金额")
    @NotNull(message = "支付金额不可为空")
    private BigDecimal amount;

    /**
     * @see AliPayParam
     * @see WeChatPayParam
     * @see VoucherPayParam
     * @see WalletPayParam
     */
    @Schema(description = "扩展参数的json字符串")
    private String extraParamsJson;
}
