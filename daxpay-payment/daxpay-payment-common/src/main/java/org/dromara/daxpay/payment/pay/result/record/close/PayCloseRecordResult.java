package org.dromara.daxpay.payment.pay.result.record.close;

import org.dromara.daxpay.platform.core.enums.pay.channel.ChannelEnum;
import org.dromara.daxpay.platform.core.enums.pay.pay.CloseTypeEnum;
import org.dromara.daxpay.payment.common.result.MchTradeBaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 支付关闭记录
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "支付关闭记录")
public class PayCloseRecordResult extends MchTradeBaseResult {

    /// 订单号
    @Schema(description = "订单号")
    private String orderNo;

    /// 商户订单号
    @Schema(description = "商户订单号")
    private String bizOrderNo;

    /// 关闭的支付通道
    /// @see ChannelEnum
    @Schema(description = "支付产品")
    private String product;

    @Schema(description = "关闭的支付通道")
    private String channel;

    /// 关闭类型 关闭/撤销
    /// @see CloseTypeEnum
    @Schema(description = "关闭类型")
    private String closeType;

    /// 是否关闭成功
    @Schema(description = "是否关闭成功")
    private boolean closed;

    /// 错误码
    @Schema(description = "错误码")
    private String errorCode;

    /// 错误信息
    @Schema(description = "错误信息")
    private String errorMsg;

    /// 终端ip
    @Schema(description = "支付终端ip")
    private String clientIp;
}

