package cn.bootx.platform.daxpay.param.pay;

import cn.bootx.platform.daxpay.code.pay.PayChannelCode;
import cn.bootx.platform.daxpay.code.pay.PayWayCode;
import cn.bootx.platform.daxpay.core.pay.convert.PayConvert;
import cn.bootx.platform.daxpay.dto.payment.PayChannelInfo;
import cn.bootx.platform.daxpay.dto.payment.RefundableInfo;
import cn.bootx.platform.daxpay.param.channel.alipay.AliPayParam;
import cn.bootx.platform.daxpay.param.channel.voucher.VoucherPayParam;
import cn.bootx.platform.daxpay.param.channel.wechat.WeChatPayParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 不只是支付, 退款发起时也是用着参数
 *
 * @author xxm
 * @date 2020/12/8
 */
@Data
@Accessors(chain = true)
@Schema(title = "支付方式参数")
public class PayModeParam implements Serializable {

    private static final long serialVersionUID = -46959864485463681L;

    /**
     * @see PayChannelCode
     */
    @Schema(description = "支付通道", required = true)
    private int payChannel;

    /**
     * @see PayWayCode
     */
    @Schema(description = "支付通道下属支付方式", required = true)
    private int payWay;

    @Schema(description = "支付金额", required = true)
    private BigDecimal amount;

    /**
     * @see AliPayParam
     * @see WeChatPayParam
     * @see VoucherPayParam
     */
    @Schema(description = "扩展参数的json字符串")
    private String extraParamsJson;

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
