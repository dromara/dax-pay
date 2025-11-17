package org.dromara.daxpay.payment.merchant.result.gateway;

import org.dromara.daxpay.payment.merchant.result.info.MchResult;
import org.dromara.daxpay.payment.pay.enums.ChannelEnum;
import org.dromara.daxpay.payment.pay.enums.PayMethodEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 网关聚合扫码支付配置
 * @author xxm
 * @since 2025/3/19
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "网关聚合扫码支付配置")
public class AggregateQrPayConfigResult extends MchResult {

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
     * @see ChannelEnum
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
     * @see ChannelEnum
     */
    @Schema(description = "银联场景对应通道")
    private String unionChannel;

    /**
     * 银联场景对应支付方式
     * @see PayMethodEnum
     */
    @Schema(description = "银联场景对应支付方式")
    private String unionMethod;

    /** 自动拉起支付 */
    @Schema(description = "自动拉起支付")
    private boolean autoLaunch;
}
