package cn.daxpay.single.service.param.record;

import cn.bootx.platform.common.core.annotation.QueryParam;
import cn.bootx.platform.common.core.rest.param.QueryOrder;
import cn.daxpay.single.core.code.PayChannelEnum;
import cn.daxpay.single.service.code.PayCallbackStatusEnum;
import cn.daxpay.single.service.code.TradeTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 支付回调信息记录
 * @author xxm
 * @since 2024/1/9
 */
@EqualsAndHashCode(callSuper = true)
@QueryParam(type = QueryParam.CompareTypeEnum.EQ)
@Data
@Accessors(chain = true)
@Schema(title = "支付回调信息记录")
public class PayCallbackRecordQuery extends QueryOrder {

    @Schema(description = "交易号")
    private String tradeNo;

    @Schema(description = "通道交易号")
    private String outTradeNo;

    /**
     * 支付通道
     * @see PayChannelEnum#getCode()
     */
    @Schema(description = "支付通道")
    private String channel;

    /**
     * 回调类型
     * @see TradeTypeEnum
     */
    @Schema(description = "回调类型")
    private String callbackType;

    /**
     * 回调处理状态
     * @see PayCallbackStatusEnum
     */
    @Schema(description = "回调处理状态")
    private String status;

    @Schema(description = "调整号")
    private String adjustNo;
}
