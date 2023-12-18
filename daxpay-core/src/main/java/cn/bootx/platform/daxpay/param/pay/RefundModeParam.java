package cn.bootx.platform.daxpay.param.pay;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.param.channel.AliPayParam;
import cn.bootx.platform.daxpay.param.channel.VoucherPayParam;
import cn.bootx.platform.daxpay.param.channel.WalletPayParam;
import cn.bootx.platform.daxpay.param.channel.WeChatPayParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

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
public class RefundModeParam {

    /**
     * @see PayChannelEnum#getCode()
     */
    @Schema(description = "支付通道编码")
    @NotBlank(message = "支付通道编码不可为空")
    private String payChannel;

    @Schema(description = "退款金额")
    @NotNull(message = "退款金额不可为空")
    private Integer amount;

    /**
     * 预留的扩展参数, 暂时未使用
     * @see AliPayParam
     * @see WeChatPayParam
     * @see VoucherPayParam
     * @see WalletPayParam
     */
    @Schema(description = "附加退款参数")
    private String channelExtra;


}
