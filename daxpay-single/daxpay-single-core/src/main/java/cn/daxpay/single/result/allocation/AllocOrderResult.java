package cn.daxpay.single.result.allocation;

import cn.daxpay.single.code.AllocOrderResultEnum;
import cn.daxpay.single.code.AllocOrderStatusEnum;
import cn.daxpay.single.code.PayChannelEnum;
import cn.daxpay.single.result.PaymentCommonResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 分账订单返回结果
 * @author xxm
 * @since 2024/5/21
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "分账订单返回结果")
public class AllocOrderResult extends PaymentCommonResult {

    /**
     * 分账单号
     */
     @Schema(description = "分账单号")
    private String allocationNo;

    /**
     * 商户分账单号
     */
     @Schema(description = "商户分账单号")
    private String bizAllocationNo;

    /**
     * 通道分账号
     */
     @Schema(description = "通道分账号")
    private String outAllocationNo;

    /**
     * 支付订单号
     */
     @Schema(description = "支付订单号")
    private String orderNo;

    /**
     * 商户支付订单号
     */
     @Schema(description = "商户支付订单号")
    private String bizOrderNo;


    /**
     * 支付订单标题
     */
    @Schema(description = "支付订单标题")
    private String title;

    /**
     * 所属通道
     * @see PayChannelEnum
     */
     @Schema(description = "所属通道")
    private String channel;

    /**
     * 总分账金额
     */
     @Schema(description = "总分账金额")
    private Integer amount;

    /**
     * 分账描述
     */
     @Schema(description = "分账描述")
    private String description;

    /**
     * 状态
     * @see AllocOrderStatusEnum
     */
     @Schema(description = "状态")
    private String status;

    /**
     * 处理结果
     * @see AllocOrderResultEnum
     */
     @Schema(description = "处理结果")
    private String result;

    /**
     * 错误码
     */
     @Schema(description = "错误码")
    private String errorCode;

    /**
     * 错误信息
     */
     @Schema(description = "错误原因")
    private String errorMsg;

    /** 分账订单完成时间 */
     @Schema(description = "分账订单完成时间")
    private LocalDateTime finishTime;

     /** 分账明细 */
     @Schema(description = "分账明细")
     private List<AllocOrderDetailResult> details;

}
