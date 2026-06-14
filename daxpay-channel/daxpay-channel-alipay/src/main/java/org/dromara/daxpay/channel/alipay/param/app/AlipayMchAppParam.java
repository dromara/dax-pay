package org.dromara.daxpay.channel.alipay.param.app;

import org.dromara.daxpay.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 支付宝直连商户应用保存参数
///
@Data
@Accessors(chain = true)
@Schema(title = "支付宝直连商户应用保存参数")
public class AlipayMchAppParam {

    @NotNull(message = "{validation.field.id.notNull}", groups = ValidationGroup.edit.class)
    @Schema(description = "主键,新增时不传")
    private Long id;

    @NotBlank(message = "{validation.field.mchNo.notBlank}")
    @Schema(description = "商户号")
    private String mchNo;

    @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}")
    @Schema(description = "通道商户号")
    private String channelMchNo;

    @NotBlank(message = "{validation.field.appName.notBlank}")
    @Schema(description = "应用名称")
    private String appName;

    @NotBlank(message = "{validation.field.aliAppId.notBlank}")
    @Schema(description = "支付宝应用ID")
    private String aliAppId;
}
