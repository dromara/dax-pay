package cn.daxpay.single.core.param.payment.transfer;

import cn.daxpay.single.core.code.PayChannelEnum;
import cn.daxpay.single.core.code.TransferPayeeTypeEnum;
import cn.daxpay.single.core.code.TransferTypeEnum;
import cn.daxpay.single.core.param.PaymentCommonParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
     * @see PayChannelEnum
     */
    @NotNull(message = "支付通道必填")
    @Size(max = 20, message = "支付通道不可超过20位")
    @Schema(description = "支付通道")
    private String channel;

    /** 转账金额 */
    @NotNull(message = "转账金额必填")
    @Min(value = 1, message = "转账金额至少为0.01元")
    @Schema(description = "转账金额")
    private Integer amount;

    /** 标题 */
    @Size(max = 100, message = "标题不可超过100位")
    @Schema(description = "标题")
    private String title;

    /** 转账原因/备注 */
    @Size(max = 150, message = "转账原因/备注不可超过150位")
    @Schema(description = "转账原因/备注")
    private String reason;

    /**
     * 转账类型, 微信使用
     * @see TransferTypeEnum
     */
    @Size(max = 20, message = "转账类型不可超过20位")
    @Schema(description = "转账类型, 微信使用")
    private String transferType;

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

    /** 回调通知地址 */
    @Size(max = 200, message = "回调通知地址不可超过200位")
    @Schema(description = "回调通知地址")
    private String notifyUrl;

    /** 商户扩展参数,回调时会原样返回 */
    @Size(max = 500, message = "商户扩展参数,回调时会原样返回不可超过500位")
    @Schema(description = "商户扩展参数,回调时会原样返回")
    private String attach;
}
