package cn.daxpay.single.param.payment.transfer;

import cn.daxpay.single.code.PayChannelEnum;
import cn.daxpay.single.code.TransferPayeeTypeEnum;
import cn.daxpay.single.code.TransferTypeEnum;
import cn.daxpay.single.param.PaymentCommonParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
    @Schema(description = "商户转账号")
    private String bizTransferNo;

    /**
     * 支付通道
     * @see PayChannelEnum
     */
    @Schema(description = "支付通道")
    private String channel;

    /** 转账金额 */
    @Schema(description = "转账金额")
    private Integer amount;

    /** 标题 */
    @Schema(description = "标题")
    private String title;

    /** 转账原因/备注 */
    @Schema(description = "转账原因/备注")
    private String reason;

    /**
     * 转账类型, 微信使用
     * @see TransferTypeEnum
     */
    @Schema(description = "转账类型, 微信使用")
    private String transferType;

    /** 付款方显示名称 */
    private String payerShowName;

    /**
     * 收款人账号类型
     * @see TransferPayeeTypeEnum
     */
    @Schema(description = "收款人账号类型")
    private String payeeType;

    /** 收款人账号 */
    @Schema(description = "收款人账号")
    private String payeeAccount;

    /** 收款人姓名 */
    @Schema(description = "收款人姓名")
    private String payeeName;

    /** 回调通知地址 */
    @Schema(description = "回调通知地址")
    private String notifyUrl;

    /** 商户扩展参数,回调时会原样返回 */
    @Schema(description = "商户扩展参数,回调时会原样返回")
    private String attach;
}
