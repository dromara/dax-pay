package cn.bootx.platform.daxpay.service.param.record;

import cn.bootx.platform.common.core.annotation.QueryParam;
import cn.bootx.platform.common.core.rest.param.QueryOrder;
import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.service.code.PayCallbackStatusEnum;
import cn.bootx.platform.daxpay.service.code.PaymentTypeEnum;
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
     * @see PaymentTypeEnum
     */
    @Schema(description = "回调类型")
    private String callbackType;

    /**
     * 回调处理状态
     * @see PayCallbackStatusEnum
     */
    @Schema(description = "回调处理状态")
    private String status;

    @Schema(description = "修复号")
    private String repairNo;
}
