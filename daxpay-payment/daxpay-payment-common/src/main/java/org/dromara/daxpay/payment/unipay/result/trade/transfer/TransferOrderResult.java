package org.dromara.daxpay.payment.unipay.result.trade.transfer;

import org.dromara.daxpay.platform.core.enums.pay.channel.ChannelEnum;
import org.dromara.daxpay.platform.core.enums.pay.transfer.TransferPayeeTypeEnum;
import org.dromara.daxpay.platform.core.enums.pay.transfer.TransferStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/// # 转账订单
///
@Data
@Accessors(chain = true)
@Schema(title = "转账订单")
public class TransferOrderResult {

    /// 商户转账号
    @Schema(description = "商户转账号")
    private String bizTransferNo;

    /// 转账号
    @Schema(description = "转账号")
    private String transferNo;

    /// 通道转账号
    @Schema(description = "通道转账号")
    private String outTransferNo;

    /// 支付通道
    /// @see ChannelEnum
    @Schema(description = "支付通道")
    private String channel;

    /// 转账金额
    @Schema(description = "转账金额")
    private BigDecimal amount;

    /// 标题
    @Schema(description = "标题")
    private String title;

    /// 转账原因/备注
    @Schema(description = "转账原因/备注")
    private String reason;

    /// 收款人类型
    /// @see TransferPayeeTypeEnum
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
    @Schema(description = "状态")
    private String status;

    /// 完成时间
    @Schema(description = "完成时间(UTC)")
    private OffsetDateTime finishTime;

    /// 商户扩展参数,回调时会原样返回
    @Schema(description = "商户扩展参数")
    private String attach;

    /// 返回转账参数, 用于拉起转账确认(微信)
    @Schema(description = "返回转账参数")
    private String transferBody;

    /// 所属进件商户号
    @Schema(description = "所属进件商户号")
    private String onbMchNo;

    /// 错误原因
    @Schema(description = "错误原因")
    private String errorMsg;

}


