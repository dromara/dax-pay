package org.dromara.daxpay.service.result.record.callback;

import org.dromara.daxpay.core.enums.CallbackStatusEnum;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.core.enums.TradeTypeEnum;
import org.dromara.daxpay.core.result.MchAppResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 回调记录
 * @author xxm
 * @since 2024/7/22
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "回调记录")
public class TradeCallbackRecordResult extends MchAppResult {

    @Schema(description = "交易号")
    private String tradeNo;

    @Schema(description = "通道交易号")
    private String outTradeNo;
    /**
     * 支付通道
     * @see ChannelEnum#getCode()
     */
    @Schema(description = "支付通道")
    private String channel;

    /**
     * 回调类型
     * @see TradeTypeEnum
     */
    @Schema(description = "回调类型")
    private String callbackType;

    /** 通知消息 */
    @Schema(description = "通知消息")
    private String notifyInfo;

    /**
     * 回调处理状态
     * @see CallbackStatusEnum
     */
    @Schema(description = "回调处理状态")
    private String status;

    /** 错误码 */
    @Schema(description = "错误码")
    private String errorCode;

    /** 提示信息 */
    @Schema(description = "提示信息")
    private String errorMsg;


}

