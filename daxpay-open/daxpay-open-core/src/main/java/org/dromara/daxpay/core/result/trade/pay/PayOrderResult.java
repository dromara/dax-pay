package org.dromara.daxpay.core.result.trade.pay;

import org.dromara.daxpay.core.enums.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付订单
 * @author xxm
 * @since 2021/2/25
 */
@Data
@Accessors(chain = true)
@Schema(title = "支付订单")
public class PayOrderResult {

    /** 商户订单号 */
    @Schema(description = "商户订单号")
    private String bizOrderNo;

    @Schema(description = "支付订单号")
    private String orderNo;

    /**
     *  通道系统交易号
     */
    @Schema(description = "通道支付订单号")
    private String outOrderNo;

    /** 标题 */
    @Schema(description = "标题")
    private String title;

    /** 描述 */
    @Schema(description = "描述")
    private String description;

    /** 是否支持分账 */
    @Schema(description = "是否需要分账")
    private Boolean allocation;

    /** 是否开启自动分账, 不传输为不开启 */
    @Schema(description = "是否开启自动分账")
    private Boolean autoAllocation;

    /**
     * 支付通道
     * @see ChannelEnum
     */
    @Schema(description = "支付通道")
    private String channel;

    /**
     * 支付方式
     */
    @Schema(description = "支付方式")
    private String method;

    /**
     * 其他支付方式, 只有在 支付方式编码(method) 为 其他支付(other)时才会生效
     * 用于处理各种通道各自定义的支付方式
     */
    @Schema(description = "其他支付方式")
    private String otherMethod;

    /**
     * 限制用户支付类型, 目前支持限制信用卡
     * @see PayLimitPayEnum
     */
    @Schema(description = "限制用户支付类型")
    private String limitPay;

    /** 金额 */
    @Schema(description = "金额")
    private BigDecimal amount;

    /** 实收金额 */
    @Schema(description = "实收金额")
    private BigDecimal realAmount;

    /** 可退款余额 */
    @Schema(description = "可退款余额")
    private BigDecimal refundableBalance;

    /**
     * 支付状态
     * @see PayStatusEnum
     */
    @Schema(description = "支付状态")
    private String status;

    /**
     * 退款状态
     * @see PayRefundStatusEnum
     */
    @Schema(description = "退款状态")
    private String refundStatus;

    /**
     * 分账状态
     * @see PayAllocStatusEnum
     */
    @Schema(description = "分账状态")
    private String allocStatus;

    /**
     * 结算状态
     * @see SettleStatusEnum
     */
    @Schema(description = "结算状态")
    private String settleStatus;

    /** 支付时间 */
    @Schema(description = "支付时间")
    private LocalDateTime payTime;

    /** 关闭时间 */
    @Schema(description = "关闭时间")
    private LocalDateTime closeTime;

    /** 过期时间 */
    @Schema(description = "过期时间")
    private LocalDateTime expiredTime;

    /** 商户扩展参数,回调时会原样返回 */
    @Schema(description = "商户扩展参数")
    private String attach;

    /** 错误信息 */
    @Schema(description = "错误信息")
    private String errorMsg;
}
