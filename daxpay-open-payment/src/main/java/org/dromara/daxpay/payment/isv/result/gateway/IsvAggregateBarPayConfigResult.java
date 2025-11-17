package org.dromara.daxpay.payment.isv.result.gateway;

import org.dromara.daxpay.payment.pay.enums.ChannelEnum;
import org.dromara.daxpay.payment.pay.enums.PayMethodEnum;
import org.dromara.daxpay.payment.merchant.result.info.MchResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 聚合付款码支付配置
 * @author xxm
 * @since 2025/3/24
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "聚合付款码支付配置")
public class IsvAggregateBarPayConfigResult extends MchResult {


    /** 是否启用聚合支付 */
    @Schema(description = "是否启用聚合支付")
    private boolean enable;

    /**
     * 微信场景对应通道
     * @see ChannelEnum
     */
    @Schema(description = "微信场景对应通道")
    private String wxChannel;

    /**
     * 微信场景对应支付方式
     * @see PayMethodEnum
     */
    @Schema(description = "微信场景对应支付方式")
    private String wxMethod;

    /**
     * 支付宝场景对应通道
     */
    @Schema(description = "支付宝场景对应通道")
    private String alipayChannel;

    /**
     * 支付宝场景对应支付方式
     * @see PayMethodEnum
     */
    @Schema(description = "支付宝场景对应支付方式")
    private String alipayMethod;

    /**
     * 银联场景对应通道
     */
    @Schema(description = "银联场景对应通道")
    private String unionChannel;

    /**
     * 银联场景对应支付方式
     * @see PayMethodEnum
     */
    @Schema(description = "银联场景对应支付方式")
    private String unionMethod;
}
