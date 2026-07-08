package cn.daxpay.open.channel.fuyou.client.req;

import cn.daxpay.open.channel.fuyou.client.credential.FuyouSdkCredential;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.OffsetDateTime;

/// # 富友退款请求(主应用侧)
@Data
public class FuyouRefundReq {

    @NotNull(message = "{validation.field.credential.notNull}")
    private FuyouSdkCredential credential;

    @NotBlank(message = "{validation.field.relationOrderNo.notBlank}")
    private String relationOrderNo;

    @NotBlank(message = "{validation.field.tradeProduct.notBlank}")
    private String tradeProduct;

    @NotBlank(message = "{validation.field.outRefundNo.notBlank}")
    private String outRefundNo;

    @NotNull(message = "{validation.field.amount.notNull}")
    @Positive(message = "{validation.field.amount.positive}")
    private Long totalAmount;

    @NotNull(message = "{validation.field.refundAmount.notNull}")
    @Positive(message = "{validation.field.refundAmount.positive}")
    private Long refundAmount;

    private String reason;

    private OffsetDateTime originPayTime;
}
