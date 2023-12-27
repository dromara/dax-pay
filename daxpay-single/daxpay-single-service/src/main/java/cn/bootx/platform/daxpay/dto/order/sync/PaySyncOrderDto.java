package cn.bootx.platform.daxpay.dto.order.sync;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.PaySyncStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 支付同步记录
 * @author xxm
 * @since 2023/7/14
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "支付同步订单")
public class PaySyncOrderDto extends BaseDto {

    /** 支付记录id */
    @Schema(description = "支付记录id")
    private Long paymentId;

    /**
     * 支付渠道
     * @see PayChannelEnum#getCode()
     */
    @Schema(description = "支付渠道")
    private String channel;

    /** 通知消息 */
    @Schema(description = "通知消息")
    private String syncInfo;

    /**
     * 同步状态
     * @see PaySyncStatusEnum
     */
    @Schema(description = "同步状态")
    private String status;

    @Schema(description = "错误消息")
    private String msg;

    /** 同步时间 */
    @Schema(description = "同步时间")
    private LocalDateTime syncTime;
}
