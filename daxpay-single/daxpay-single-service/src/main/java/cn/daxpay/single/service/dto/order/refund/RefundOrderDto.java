package cn.daxpay.single.service.dto.order.refund;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import cn.daxpay.single.code.PayChannelEnum;
import cn.daxpay.single.code.RefundStatusEnum;
import cn.daxpay.single.param.channel.AliPayParam;
import cn.daxpay.single.param.channel.WalletPayParam;
import cn.daxpay.single.param.channel.WeChatPayParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 退款记录
 *
 * @author xxm
 * @since 2022/3/2
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "退款记录")
public class RefundOrderDto extends BaseDto {

    /** 支付订单ID */
    @Schema(description = "支付订单ID")
    private Long orderId;

    /** 支付订单号 */
    @Schema(description = "支付订单号")
    private String orderNo;

    /** 通道支付订单号 */
    @Schema(description = "通道支付订单号")
    private String outOrderNo;

    /** 商户支付订单号 */
    @Schema(description = "商户支付订单号")
    private String bizOrderNo;

    /** 支付标题 */
    @Schema(description = "支付标题")
    private String title;

    /** 退款号 */
    @Schema(description = "退款号")
    private String refundNo;

    /** 商户退款号 */
    @Schema(description = "商户退款号")
    private String bizRefundNo;

    /** 通道退款交易号 */
    @Schema(description = "通道退款交易号")
    private String outRefundNo;

    /**
     * 退款通道
     * @see PayChannelEnum
     */
    @Schema(description = "支付通道")
    private String channel;

    /** 订单金额 */
    @Schema(description = "订单金额")
    private Integer orderAmount;

    /** 退款金额 */
    @Schema(description = "退款金额")
    private Integer amount;

    /** 退款原因 */
    @Schema(description = "退款原因")
    private String reason;

    /** 退款结束时间 */
    @Schema(description = "退款结束时间")
    private LocalDateTime finishTime;

    /**
     * 退款状态
     * @see RefundStatusEnum
     */
    @Schema(description = "退款状态")
    private String status;

    /** 异步通知地址 */
    @Schema(description = "异步通知地址")
    private String notifyUrl;

    /** 商户扩展参数,回调时会原样返回, 以最后一次为准 */
    @Schema(description = "商户扩展参数,回调时会原样返回, 以最后一次为准")
    private String attach;

    /**
     * 附加参数 以最后一次为准
     * @see AliPayParam
     * @see WeChatPayParam
     * @see WalletPayParam
     */
    @Schema(description = "附加参数 以最后一次为准")
    private String extraParam;

    /** 请求时间，时间戳转时间 */
    @Schema(description = "请求时间，时间戳转时间")
    private LocalDateTime reqTime;

    /** 终端ip */
    @Schema(description = "终端ip")
    private String clientIp;

    /** 错误码 */
    @Schema(description = "错误码")
    private String errorCode;

    /** 错误信息 */
    @Schema(description = "错误信息")
    private String errorMsg;

}
