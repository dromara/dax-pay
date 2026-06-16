package org.dromara.daxpay.payment.old.pay.result.masterdata.provider;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/// # 渠道支持的支付方式
@Data
@Accessors(chain = true)
@Schema(title = "渠道支付方式")
public class PayProviderMethodResult {

    @Schema(description = "支付渠道编码")
    private String provider;

    @Schema(description = "支付方式编码")
    private String method;

    @Schema(description = "支付方式展示名")
    private String methodLabel;

    @Schema(description = "渠道内排序")
    private Integer sortNo;

    @Schema(description = "目录项说明")
    private String description;

    @Schema(description = "支持的支付产品")
    private List<PayProviderProductResult> supportedProducts;
}