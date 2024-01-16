package cn.bootx.platform.daxpay.result.pay;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.result.CommonResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 统一下单响应参数
 * @author xxm
 * @since 2023/12/17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "统一下单响应参数")
public class PayResult extends CommonResult {

    @Schema(description = "支付ID")
    private Long paymentId;

    @Schema(description = "是否是异步支付")
    private boolean asyncPay;

    /**
     * @see PayChannelEnum#ASYNC_TYPE_CODE
     */
    @Schema(description = "异步支付通道")
    private String asyncPayChannel;


    /** 支付参数体(通常用于发起异步支付的参数) */
    @Schema(description = "支付参数体")
    private String payBody;

    /**
     * @see PayStatusEnum
     */
    @Schema(description = "支付状态")
    private String status;

}
