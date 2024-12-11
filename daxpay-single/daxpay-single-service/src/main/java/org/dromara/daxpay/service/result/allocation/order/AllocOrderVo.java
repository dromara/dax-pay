package org.dromara.daxpay.service.result.allocation.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.dromara.daxpay.core.enums.AllocationResultEnum;
import org.dromara.daxpay.core.enums.AllocationStatusEnum;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.core.result.MchAppResult;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 分账订单
 * @author xxm
 * @since 2024/5/21
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "分账订单")
public class AllocOrderVo extends MchAppResult {

    /** 分账单号 */
    @Schema(description = "分账单号")
    private String allocNo;

    /** 商户分账单号 */
    @Schema(description = "商户分账单号")
    private String bizAllocNo;

    /** 通道分账号 */
    @Schema(description = "通道分账号")
    private String outAllocNo;

    /**
     * 支付订单号
     */
    @Schema(description = "支付订单号")
    private String orderNo;

    /** 商户支付订单号 */
    @Schema(description = "商户支付订单号")
    private String bizOrderNo;

    /** 通道支付订单号 */
    @Schema(description = "通道支付订单号")
    private String outOrderNo;

    /**
     * 支付订单标题
     */
    @Schema(description = "支付订单标题")
    private String title;

    /**
     * 所属通道
     * @see ChannelEnum
     */
    @Schema(description = "所属通道")
    private String channel;

    /**
     * 总分账金额
     */
    @Schema(description = "总分账金额")
    private BigDecimal amount;

    /**
     * 分账描述
     */
    @Schema(description = "分账描述")
    private String description;

    /**
     * 状态
     * @see AllocationStatusEnum
     */
    @Schema(description = "状态")
    private String status;

    /**
     * 处理结果
     * @see AllocationResultEnum
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

}
