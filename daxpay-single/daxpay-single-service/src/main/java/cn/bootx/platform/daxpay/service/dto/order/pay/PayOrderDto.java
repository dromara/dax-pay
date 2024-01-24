package cn.bootx.platform.daxpay.service.dto.order.pay;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import cn.bootx.platform.daxpay.code.PayChannelEnum;
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

    /** 关联的业务号 */
    @Schema(description = "关联的业务号")
    private String businessNo;

    /** 标题 */
    @Schema(description = "标题")
    private String title;

    /** 是否是异步支付 */
    @Schema(description = "是否是异步支付")
    private boolean asyncPay;

    /** 是否是组合支付 */
    @Schema(description = "是否是组合支付")
    private boolean combinationPay;

    /**
     * 异步支付通道
     * @see PayChannelEnum#ASYNC_TYPE_CODE
     */
    @Schema(description = "异步支付通道")
    private String asyncChannel;

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

    /** 支付时间 */
    @Schema(description = "支付时间")
    private LocalDateTime payTime;

    /** 过期时间 */
    @Schema(description = "过期时间")
    private LocalDateTime expiredTime;

}
