package org.dromara.daxpay.payment.pay.param.order.transfer;

import org.dromara.daxpay.platform.core.annotation.QueryParam;
import org.dromara.daxpay.platform.core.enums.pay.channel.ChannelEnum;
import org.dromara.daxpay.platform.core.enums.pay.transfer.TransferPayeeTypeEnum;
import org.dromara.daxpay.platform.core.enums.pay.transfer.TransferStatusEnum;
import org.dromara.daxpay.payment.pay.param.MchQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 转账订单查询参数
///
@QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "转账订单查询参数")
public class TransferOrderQuery extends MchQuery {

    /// 商户转账号
    @Schema(description = "商户转账号")
    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    private String bizTransferNo;

    /// 转账号
    @Schema(description = "转账号")
    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    private String transferNo;

    /// 通道转账号
    @Schema(description = "通道转账号")
    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    private String outTransferNo;


    /// 支付产品
    /// @see org.dromara.daxpay.platform.core.enums.pay.channel.ProductEnum
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "支付产品")
    private String product;

    /// 支付通道
    /// @see ChannelEnum
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "支付通道")
    private String channel;

    /// 标题
    @Schema(description = "标题")
    private String title;

    /// 收款人类型
    /// @see TransferPayeeTypeEnum
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "收款人类型")
    private String payeeType;

    /// 收款人账号
    @Schema(description = "收款人账号")
    private String payeeAccount;

    /// 收款人姓名
    @Schema(description = "收款人姓名")
    private String payeeName;

    /// 状态
    /// @see TransferStatusEnum
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "状态")
    private String status;
}

