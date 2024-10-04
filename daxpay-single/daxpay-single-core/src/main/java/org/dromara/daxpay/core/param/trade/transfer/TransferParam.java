package org.dromara.daxpay.core.param.trade.transfer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.core.enums.TransferPayeeTypeEnum;
import org.dromara.daxpay.core.param.PaymentCommonParam;

import java.math.BigDecimal;


/**
 * 转账参数
 * @author xxm
 * @since 2024/5/26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "转账参数")
public class TransferParam extends PaymentCommonParam {

    /** 商户转账号 */
    @NotNull(message = "商户转账号必填")
    @Size(max = 100, message = "商户转账号不可超过100位")
    @Schema(description = "商户转账号")
    private String bizTransferNo;

    /**
     * 支付通道
     * @see ChannelEnum
     */
    @NotNull(message = "支付通道必填")
    @Size(max = 20, message = "支付通道不可超过20位")
    @Schema(description = "支付通道")
    private String channel;

    /** 转账金额 */
    @Schema(description = "转账金额")
    @NotNull(message = "转账金额必填")
    @DecimalMin(value = "0.01", message = "支付金额不可小于0.01元")
    @Digits(integer = 8, fraction = 2, message = "支付金额精度到分, 且要小于一亿元")
    private BigDecimal amount;

    /** 标题 */
    @Size(max = 100, message = "标题不可超过100位")
    @Schema(description = "标题")
    private String title;

    /** 转账原因/备注 */
    @Size(max = 150, message = "转账原因/备注不可超过150位")
    @Schema(description = "转账原因/备注")
    private String reason;

    /**
     * 收款人账号类型
     * @see TransferPayeeTypeEnum
     */
    @NotBlank(message = "收款人账号类型必填")
    @Size(max = 20, message = "收款人账号类型不可超过20位")
    @Schema(description = "收款人账号类型")
    private String payeeType;

    /** 收款人账号 */
    @NotBlank(message = "收款人账号必填")
    @Size(max = 100, message = "收款人账号不可超过100位")
    @Schema(description = "收款人账号")
    private String payeeAccount;

    /** 收款人姓名 */
    @Size(max = 50, message = "收款人姓名不可超过50位")
    @Schema(description = "收款人姓名")
    private String payeeName;

    /**
     * 预留的转账扩展参数
     */
    @Schema(description = "转账扩展参数")
    @Size(max = 2048, message = "退转账扩展参数不可超过2048位")
    private String extraParam;

    /** 商户扩展参数,回调时会原样返回 */
    @Size(max = 500, message = "商户扩展参数,回调时会原样返回不可超过500位")
    @Schema(description = "商户扩展参数,回调时会原样返回")
    private String attach;

    /** 回调通知地址 */
    @Size(max = 200, message = "回调通知地址不可超过200位")
    @Schema(description = "回调通知地址")
    private String notifyUrl;

}
