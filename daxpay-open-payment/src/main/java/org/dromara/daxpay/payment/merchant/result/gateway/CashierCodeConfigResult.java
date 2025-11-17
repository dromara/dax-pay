package org.dromara.daxpay.payment.merchant.result.gateway;

import org.dromara.daxpay.payment.pay.enums.ChannelEnum;
import org.dromara.daxpay.payment.pay.enums.PayMethodEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 收银码牌配置
 * @author xxm
 * @since 2024/11/20
 */
@Data
@Accessors(chain = true)
@Schema(title = "收银码牌配置")
public class CashierCodeConfigResult {

    /** 主键 */
    @Schema(description = "主键")
    private Long id;

    /** 是否启用 */
    @Schema(description = "是否启用")
    private boolean enable;

    /** 限制用户支付方式 */
    @Schema(description = "限制用户支付方式")
    private String limitPay;

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

    /** 创建时间 */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /** 更新时间 */
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
