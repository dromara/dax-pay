package cn.bootx.platform.daxpay.result.order;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.result.CommonResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 支付单响应参数
 * @author xxm
 * @since 2024/1/16
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "支付单响应参数")
public class PayOrderResult extends CommonResult {

    /** 关联的业务号 */
    @Schema(description = "关联的业务号")
    private String businessNo;

    /** 标题 */
    @Schema(description = "标题")
    private String title;

    /** 描述 */
    @Schema(description = "描述")
    private String description;

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

    @Schema(description = "支付通道列表")
    private List<PayOrderChannelResult> channels;
}
