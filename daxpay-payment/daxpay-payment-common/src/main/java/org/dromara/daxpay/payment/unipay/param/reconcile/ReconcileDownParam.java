package org.dromara.daxpay.payment.unipay.param.reconcile;

import org.dromara.daxpay.payment.unipay.param.MerchantPaymentCommonParam;
import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/// # 对账文件下载参数
///
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "对账下载参数")
public class ReconcileDownParam extends MerchantPaymentCommonParam {
    @Schema(description = "支付产品")
    @Size(max = 32, message = "{validation.field.product.size}")
    @NotBlank(message = "{validation.field.product.notBlank}")
    private String product;

    @Schema(description = "通道")
    @Size(max = 32, message = "{validation.field.channel.size}")
    private String channel;

    @Schema(description = "日期")
    @NotNull(message = "{validation.field.date.notNull}")
    @JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
    private LocalDate date;
}
