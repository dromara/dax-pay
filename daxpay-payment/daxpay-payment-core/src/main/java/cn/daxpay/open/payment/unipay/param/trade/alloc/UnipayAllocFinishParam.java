package cn.daxpay.open.payment.unipay.param.trade.alloc;

import cn.daxpay.open.payment.unipay.param.MerchantPaymentCommonParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/// # 统一分账完结参数(对外签名)
///
/// 商户系统通过 RSA 签名调用 /unipay/alloc-finish, 对已分账未完结(多次分账)的订单
/// 发起完结, 解冻剩余冻结资金给商户本身。
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "统一分账完结参数")
public class UnipayAllocFinishParam extends MerchantPaymentCommonParam {

    /// 商户分账单号(幂等键, 同一应用下唯一, 与分账单共用维度)
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

    /// 完结描述
    @Schema(description = "完结描述")
    @Size(max = 500, message = "{validation.field.description.size}")
    private String description;

    /// 商户扩展参数, 回调时原样返回
    @Schema(description = "商户扩展参数")
    @Size(max = 500, message = "{validation.field.attach.size}")
    private String attach;

    /// 异步通知地址
    @Schema(description = "异步通知地址")
    @Size(max = 200, message = "{validation.field.notifyUrl.size}")
    private String notifyUrl;
}
