package org.dromara.daxpay.payment.merchant.param.gateway;

import org.dromara.daxpay.payment.pay.enums.ChannelEnum;
import org.dromara.daxpay.payment.pay.enums.PayMethodEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 收银码牌配置
 * @author xxm
 * @since 2024/11/20
 */
@Data
@Accessors(chain = true)
@Schema(title = "收银码牌配置")
public class CashierCodeConfigParam {

    @NotBlank(message = "商户应用编号不能为空")
    @Schema(description = "商户应用编号")
    private String appId;

    /** 限制用户支付方式 */
    @Schema(description = "限制用户支付方式")
    @Size(max = 128, message = "限制用户支付方式不能超过128位")
    private String limitPay;

    /**
     * 微信场景对应通道
     * @see ChannelEnum
     */
    @Schema(description = "微信场景对应通道")
    @Size(max = 32, message = "微信场景对应通道不能超过32位")
    private String wxChannel;

    /**
     * 微信场景对应支付方式
     * @see PayMethodEnum
     */
    @Schema(description = "微信场景对应支付方式")
    @Size(max = 32, message = "微信场景对应支付方式不能超过32位")
    private String wxMethod;

    /**
     * 支付宝场景对应通道
     */
    @Schema(description = "支付宝场景对应通道")
    @Size(max = 32, message = "支付宝场景对应通道不能超过32位")
    private String alipayChannel;

    /**
     * 支付宝场景对应支付方式
     * @see PayMethodEnum
     */
    @Schema(description = "支付宝场景对应支付方式")
    @Size(max = 32, message = "支付宝场景对应支付方式不能超过32位")
    private String alipayMethod;

    /**
     * 银联场景对应通道
     */
    @Schema(description = "银联场景对应通道")
    @Size(max = 32, message = "银联场景对应通道不能超过32位")
    private String unionChannel;

    /**
     * 银联场景对应支付方式
     * @see PayMethodEnum
     */
    @Schema(description = "银联场景对应支付方式")
    @Size(max = 32, message = "银联场景对应支付方式不能超过32位")
    private String unionMethod;
}
