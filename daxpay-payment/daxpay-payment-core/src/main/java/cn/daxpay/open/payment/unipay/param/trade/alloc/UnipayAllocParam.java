package cn.daxpay.open.payment.unipay.param.trade.alloc;

import cn.daxpay.open.payment.unipay.param.MerchantPaymentCommonParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

/// # 统一分账参数(对外签名)
///
/// 商户系统通过 RSA 签名调用 /unipay/alloc 发起分账。
/// 接收方列表直接传入完整明细(极简模式, 接收方绑定由调用方提前在通道侧完成)。
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "统一分账参数")
public class UnipayAllocParam extends MerchantPaymentCommonParam {

    /// 商户分账单号(幂等键, 同一应用下唯一)
    @Schema(description = "商户分账单号")
    @NotBlank(message = "{validation.field.bizAllocNo.notBlank}")
    @Size(max = 100, message = "{validation.field.bizAllocNo.size}")
    private String bizAllocNo;

    /// 原支付资金交易号(tradeNo 与 bizOrderNo 二选一, tradeNo 优先)
    @Schema(description = "原支付资金交易号")
    @Size(max = 100, message = "{validation.field.tradeNo.size}")
    private String tradeNo;

    /// 原支付商户业务订单号(tradeNo 为空时用此定位原支付)
    @Schema(description = "原支付商户业务订单号")
    @Size(max = 100, message = "{validation.field.bizOrderNo.size}")
    private String bizOrderNo;

    /// 分账标题
    @Schema(description = "分账标题")
    @Size(max = 100, message = "{validation.field.title.size}")
    private String title;

    /// 分账描述
    @Schema(description = "分账描述")
    @Size(max = 500, message = "{validation.field.description.size}")
    private String description;

    /// 接收方列表(至少一个)
    @NotEmpty(message = "{validation.field.receivers.notEmpty}")
    @Valid
    @Schema(description = "接收方列表")
    private List<ReceiverParam> receivers;

    /// 商户扩展参数, 回调时原样返回
    @Schema(description = "商户扩展参数")
    @Size(max = 500, message = "{validation.field.attach.size}")
    private String attach;

    /// 异步通知地址
    @Schema(description = "异步通知地址")
    @Size(max = 200, message = "{validation.field.notifyUrl.size}")
    private String notifyUrl;

    /// 分账接收方参数(单个)
    @Data
    @Schema(title = "分账接收方参数")
    public static class ReceiverParam {

        /// 接收方类型
        /// @see cn.daxpay.open.payment.trade.alloc.enums.AllocReceiverTypeEnum
        @Schema(description = "接收方类型")
        @NotBlank(message = "{validation.field.receiverType.notBlank}")
        @Size(max = 32, message = "{validation.field.receiverType.size}")
        private String receiverType;

        /// 接收方账号
        @Schema(description = "接收方账号")
        @NotBlank(message = "{validation.field.receiverAccount.notBlank}")
        @Size(max = 128, message = "{validation.field.receiverAccount.size}")
        private String receiverAccount;

        /// 接收方姓名(部分通道/类型必填)
        @Schema(description = "接收方姓名")
        @Size(max = 64, message = "{validation.field.receiverName.size}")
        private String receiverName;

        /// 分账金额(元)
        @Schema(description = "分账金额(元)")
        @NotNull(message = "{validation.field.amount.notNull}")
        @DecimalMin(value = "0.01", message = "{validation.field.amount.min}")
        @Digits(integer = 8, fraction = 2, message = "{validation.field.amount.digits}")
        private BigDecimal amount;
    }
}
