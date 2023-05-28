package cn.bootx.platform.daxpay.param.pay;

import cn.bootx.platform.daxpay.code.pay.PayChannelEnum;
import cn.bootx.platform.daxpay.code.pay.PayWayEnum;
import cn.bootx.platform.daxpay.core.pay.convert.PayConvert;
import cn.bootx.platform.daxpay.dto.payment.PayChannelInfo;
import cn.bootx.platform.daxpay.dto.payment.RefundableInfo;
import cn.bootx.platform.daxpay.param.channel.alipay.AliPayParam;
import cn.bootx.platform.daxpay.param.channel.voucher.VoucherPayParam;
import cn.bootx.platform.daxpay.param.channel.wallet.WalletPayParam;
import cn.bootx.platform.daxpay.param.channel.wechat.WeChatPayParam;
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
 * @date 2020/12/8
 */
@Data
@Accessors(chain = true)
@Schema(title = "支付方式参数")
public class PayWayParam implements Serializable {

    private static final long serialVersionUID = -46959864485463681L;

    /**
     * @see PayChannelEnum#getCode()
     */
    @Schema(description = "支付通道编码")
    @NotBlank(message = "支付通道编码不可为空")
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

    /**
     * 转换为 支付通道信息 对象
     */
    public PayChannelInfo toPayTypeInfo() {
        return PayConvert.CONVERT.convert(this);
    }

    /**
     * 转换为可退款信息
     */
    public RefundableInfo toRefundableInfo() {
        return new RefundableInfo().setPayChannel(getPayChannel()).setAmount(getAmount());
    }

}
