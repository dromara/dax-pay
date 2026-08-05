package cn.daxpay.open.payment.trade.transfer.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/// # 转账发起参数(管理端/商户端共用)
///
/// 各通道差异校验下沉到通道策略 [cn.daxpay.open.payment.strategy.transfer.AbsTransferStrategy]，
/// 如微信要求 [payeeType]=openid、金额档位姓名校验等。
@Data
@Accessors(chain = true)
@Schema(title = "转账发起参数")
public class TransferParam {

    /// 商户号(运营端代发必填; 商户端由登录上下文强制, 忽略入参)
    @Size(max = 32, message = "商户号不可超过32位")
    @Schema(description = "商户号")
    private String mchNo;

    /// 应用号(可空, 默认取商户默认应用)
    @Size(max = 32, message = "应用号不可超过32位")
    @Schema(description = "应用号")
    private String appId;

    /// 通道商户号(凭证组装与通道路由用)
    @NotBlank(message = "通道商户号必填")
    @Size(max = 64, message = "通道商户号不可超过64位")
    @Schema(description = "通道商户号")
    private String channelMchNo;

    /// 商户转账号(幂等键, 同一应用下唯一; FAIL 后复用原单号重试)
    @NotBlank(message = "商户转账号必填")
    @Size(max = 100, message = "商户转账号不可超过100位")
    @Schema(description = "商户转账号")
    private String bizTransferNo;

    /// 转账金额(元)
    @NotNull(message = "转账金额必填")
    @DecimalMin(value = "0.01", message = "转账金额不可小于0.01元")
    @Digits(integer = 8, fraction = 2, message = "转账金额精度到分, 且要小于一亿元")
    @Schema(description = "转账金额(元)")
    private BigDecimal amount;

    /// 转账标题
    @Size(max = 100, message = "转账标题不可超过100位")
    @Schema(description = "转账标题")
    private String title;

    /// 转账原因/备注
    @Size(max = 200, message = "转账原因不可超过200位")
    @Schema(description = "转账原因/备注")
    private String reason;

    /// 收款人账号类型
    /// @see cn.daxpay.open.payment.trade.transfer.enums.TransferPayeeTypeEnum
    @NotBlank(message = "收款人账号类型必填")
    @Size(max = 32, message = "收款人账号类型不可超过32位")
    @Schema(description = "收款人账号类型")
    private String payeeType;

    /// 收款人账号
    @NotBlank(message = "收款人账号必填")
    @Size(max = 100, message = "收款人账号不可超过100位")
    @Schema(description = "收款人账号")
    private String payeeAccount;

    /// 收款人姓名(微信: 小于0.3元禁填, 大于等于2000元必填)
    @Size(max = 100, message = "收款人姓名不可超过100位")
    @Schema(description = "收款人姓名")
    private String payeeName;

    /// 商户扩展参数, 回调时原样返回
    @Size(max = 500, message = "商户扩展参数不可超过500位")
    @Schema(description = "商户扩展参数, 回调时原样返回")
    private String attach;

    /// 回调通知地址
    @Size(max = 200, message = "回调通知地址不可超过200位")
    @Schema(description = "回调通知地址")
    private String notifyUrl;
}
