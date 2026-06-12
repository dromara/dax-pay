package org.dromara.daxpay.payment.unipay.param.trade.transfer;

import org.dromara.daxpay.platform.core.enums.pay.channel.ChannelEnum;
import org.dromara.daxpay.platform.core.enums.pay.channel.ProductEnum;
import org.dromara.daxpay.platform.core.enums.pay.trade.TradeSourceEnum;
import org.dromara.daxpay.platform.core.enums.pay.transfer.TransferPayeeTypeEnum;
import org.dromara.daxpay.payment.unipay.param.MerchantPaymentCommonParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/// # 转账参数
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "转账参数")
public class TransferParam extends MerchantPaymentCommonParam {

    /// 商户转账号
    @NotNull(message = "{validation.field.bizTransferNo.notNull}")
    @Size(max = 100, message = "{validation.field.bizTransferNo.size}")
    @Schema(description = "商户转账号")
    private String bizTransferNo;

    /// 支付产品
    /// @see ProductEnum
    @NotNull(message = "{validation.field.product.notNull}")
    @Size(max = 32, message = "{validation.field.product.size}")
    @Schema(description = "支付产品")
    private String product;

    /// 支付通道
    /// @see ChannelEnum
    @Size(max = 32, message = "{validation.field.channel.size}")
    @Schema(description = "支付通道")
    private String channel;

    /// 转账金额
    @Schema(description = "转账金额")
    @NotNull(message = "{validation.field.amount.notNull}")
    @DecimalMin(value = "0.01", message = "{validation.field.amount.decimalMin}")
    @Digits(integer = 8, fraction = 2, message = "{validation.field.amount.digits}")
    private BigDecimal amount;

    /// 标题
    @Size(max = 100, message = "{validation.field.title.size}")
    @Schema(description = "标题")
    private String title;

    /// 转账原因/备注
    @Size(max = 50, message = "{validation.field.reason.size}")
    @Schema(description = "转账原因/备注")
    private String reason;

    /// 收款人账号类型
    /// @see TransferPayeeTypeEnum
    @NotBlank(message = "{validation.field.payeeType.notBlank}")
    @Size(max = 32, message = "{validation.field.payeeType.size}")
    @Schema(description = "收款人账号类型")
    private String payeeType;

    /// 收款人账号
    @NotBlank(message = "{validation.field.payeeAccount.notBlank}")
    @Size(max = 100, message = "{validation.field.payeeAccount.size}")
    @Schema(description = "收款人账号")
    private String payeeAccount;

    /// 收款人姓名
    @Size(max = 50, message = "{validation.field.payeeName.size}")
    @Schema(description = "收款人姓名")
    private String payeeName;

    /// 预留的转账扩展参数
    @Schema(description = "转账扩展参数")
    @Size(max = 2048, message = "{validation.field.extraParam.size}")
    private String extraParam;

    /// 商户扩展参数,回调时会原样返回
    @Size(max = 500, message = "{validation.field.attach.size}")
    @Schema(description = "商户扩展参数,回调时会原样返回")
    private String attach;

    /// 回调通知地址
    @Size(max = 200, message = "{validation.field.notifyUrl.size}")
    @Schema(description = "回调通知地址")
    private String notifyUrl;

    /// 订单来源
    /// @see TradeSourceEnum
    @Schema(description = "订单来源", hidden = true)
    @Null(message = "{validation.field.orderSource.mustBeNull}")
    private String source;

}

