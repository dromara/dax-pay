package org.dromara.daxpay.payment.isv.param.gateway;

import org.dromara.daxpay.payment.pay.enums.ChannelEnum;
import org.dromara.daxpay.payment.pay.enums.PayMethodEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 网关聚合支付配置参数
 * @author xxm
 * @since 2024/11/27
 */
@Data
@Accessors(chain = true)
@Schema(title = "网关聚合支付配置参数")
public class IsvAggregatePayConfigParam {

    /** 自动拉起支付 */
    @Schema(description = "自动拉起支付")
    @NotNull(message = "自动拉起支付不可为空")
    private Boolean autoLaunch;

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

    /** 服务商号 */
    @NotBlank(message = "服务商号不可为空")
    @Size(max = 32, message = "服务商号不可超过32位")
    @Schema(description = "服务商号")
    private String isvNo;
}
