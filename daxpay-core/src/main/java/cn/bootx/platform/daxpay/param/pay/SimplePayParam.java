package cn.bootx.platform.daxpay.param.pay;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.PayWayEnum;
import cn.bootx.platform.daxpay.param.channel.AliPayParam;
import cn.bootx.platform.daxpay.param.channel.VoucherPayParam;
import cn.bootx.platform.daxpay.param.channel.WalletPayParam;
import cn.bootx.platform.daxpay.param.channel.WeChatPayParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 简单下单参数
 * @author xxm
 * @since 2023/12/17
 */
@Data
@Schema(title = "简单下单参数")
public class SimplePayParam {

    @Schema(description = "业务号")
    @NotBlank(message = "业务号不可为空")
    private String businessNo;

    @Schema(description = "支付标题")
    @NotBlank(message = "支付标题不可为空")
    private String title;

    @Schema(description = "支付描述")
    private String description;

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
    @Schema(description = "附加支付参数")
    private String channelExtra;


}
