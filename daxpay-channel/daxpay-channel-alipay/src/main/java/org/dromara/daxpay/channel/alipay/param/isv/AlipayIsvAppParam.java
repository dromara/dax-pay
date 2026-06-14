package org.dromara.daxpay.channel.alipay.param.isv;

import org.dromara.daxpay.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 支付宝服务商应用保存参数
///
/// 保存/更新支付宝服务商应用时接收的请求参数，含应用名称和支付宝应用ID，新增时不传主键。
///
@Data
@Accessors(chain = true)
@Schema(title = "支付宝服务商应用保存参数")
public class AlipayIsvAppParam {

    @NotNull(message = "{validation.field.id.notNull}", groups = ValidationGroup.edit.class)
    @Schema(description = "主键,新增时不传")
    private Long id;

    @NotBlank(message = "{validation.field.appName.notBlank}")
    @Schema(description = "应用名称")
    private String appName;

    @NotBlank(message = "{validation.field.aliAppId.notBlank}")
    @Schema(description = "支付宝应用ID")
    private String aliAppId;
}
