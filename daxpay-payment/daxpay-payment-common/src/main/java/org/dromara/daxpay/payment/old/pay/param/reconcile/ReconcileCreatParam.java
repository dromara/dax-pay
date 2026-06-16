package org.dromara.daxpay.payment.old.pay.param.reconcile;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/// # 对账任务创建参数
///
@Data
@Accessors(chain = true)
@Schema(title = "对账任务创建参数")
public class ReconcileCreatParam {

    /// 名称
    @Schema(description = "名称")
    @NotBlank(message = "{validation.field.title.notBlank}")
    private String title;

    /// 支付产品
    @Schema(description = "支付产品")
    @NotBlank(message = "{validation.field.product.notBlank}")
    private String product;

    /// 通道
    @Schema(description = "通道")
    private String channel;

    /// 日期
    @Schema(description = "日期")
    @NotNull(message = "{validation.field.date.notNull}")
    private LocalDate date;

    /// 商户号
    @Schema(description = "商户号")
    @NotBlank(message = "{validation.field.mchNo.notBlank}")
    private String mchNo;

    /// 应用Appid
    @Schema(description = "应用Appid")
    @NotBlank(message = "{validation.field.appId.notBlank}")
    private String appId;

}
