package cn.daxpay.open.payment.old.pay.param.order.pay;

import cn.daxpay.open.platform.core.annotation.QueryParam;
import cn.daxpay.open.platform.core.enums.pay.channel.ChannelEnum;
import cn.daxpay.open.platform.core.enums.pay.pay.PayRefundStatusEnum;
import cn.daxpay.open.platform.core.enums.pay.pay.PayStatusEnum;
import cn.daxpay.open.payment.old.pay.param.MchQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 支付订单查询参数
///
@EqualsAndHashCode(callSuper = true)
@Data
@QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
@Accessors(chain = true)
@Schema(title = "支付订单查询参数")
public class PayOrderQuery extends MchQuery {

    /// 商户订单号
    @Schema(description = "商户订单号")
    private String bizOrderNo;

    /// 支付订单号
    @Schema(description = "支付订单号")
    private String orderNo;

    /// 通道系统交易号
    @Schema(description = "通道支付订单号")
    private String outOrderNo;

    /// 标题
    @Schema(description = "标题")
    private String title;

    /// 支付产品
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "支付产品")
    private String product;

    /// 支付通道
    /// @see ChannelEnum
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "支付通道")
    private String channel;

    /// 支付方式
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "支付方式")
    private String method;

    /// 支付状态
    /// @see PayStatusEnum
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "支付状态")
    private String status;

    /// 退款状态
    /// @see PayRefundStatusEnum
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "退款状态")
    private String refundStatus;

    /// 错误码
    @Schema(description = "错误码")
    private String errorCode;

}

