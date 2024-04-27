package cn.bootx.platform.daxpay.service.dto.order.pay;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.PayOrderAllocationStatusEnum;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author xxm
 * @since 2021/2/25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "具体支付日志基类")
public class PayOrderDto extends BaseDto {

    /** 商户订单号 */
    @Schema(description = "商户订单号")
    private String bizOrderNo;

    @Schema(description = "支付订单号")
    private String orderNo;

    /**
     *  外部系统交易号
     */
    @Schema(description = "外部支付订单号")
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

    /**
     * 支付通道
     * @see PayChannelEnum
     */
    @Schema(description = "异步支付通道")
    private String channel;

    /**
     * 支付方式
     */
    @Schema(description = "支付方式")
    private String method;

    /** 金额 */
    @Schema(description = "金额")
    private Integer amount;

    /** 可退款余额 */
    @Schema(description = "可退款余额")
    private Integer refundableBalance;

    /**
     * 支付状态
     * @see PayStatusEnum
     */
    @Schema(description = "支付状态")
    private String status;

    /**
     * 分账状态
     * @see PayOrderAllocationStatusEnum
     */
    @Schema(description = "分账状态")
    private String allocationStatus;

    /** 支付时间 */
    @Schema(description = "支付时间")
    private LocalDateTime payTime;

    /** 关闭时间 */
    @Schema(description = "关闭时间")
    private LocalDateTime closeTime;

    /** 过期时间 */
    @Schema(description = "过期时间")
    private LocalDateTime expiredTime;

    /** 错误码 */
    @Schema(description = "错误码")
    private String errorCode;

    /** 错误信息 */
    @Schema(description = "错误信息")
    private String errorMsg;


}
