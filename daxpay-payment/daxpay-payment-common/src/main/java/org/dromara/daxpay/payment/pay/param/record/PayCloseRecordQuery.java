package org.dromara.daxpay.payment.pay.param.record;

import org.dromara.daxpay.platform.core.annotation.QueryParam;
import org.dromara.daxpay.platform.core.enums.pay.channel.ChannelEnum;
import org.dromara.daxpay.payment.pay.param.MchQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 支付关闭记录
///
@QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "支付关闭记录")
public class PayCloseRecordQuery extends MchQuery {

    /// 订单号
    @Schema(description = "订单号")
    private String orderNo;

    /// 商户订单号
    @Schema(description = "商户订单号")
    private String bizOrderNo;


    /// 支付产品
    /// @see org.dromara.daxpay.platform.core.enums.pay.channel.ProductEnum
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "支付产品")
    private String product;

    /// 关闭的支付通道
    /// @see ChannelEnum
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "关闭的支付通道")
    private String channel;

    /// 是否关闭成功
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "是否关闭成功")
    private Boolean closed;
}

