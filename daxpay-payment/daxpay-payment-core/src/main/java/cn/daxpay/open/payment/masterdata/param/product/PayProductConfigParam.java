package cn.daxpay.open.payment.masterdata.param.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 支付产品配置保存参数
///
@Data
@Accessors(chain = true)
@Schema(title = "支付产品配置保存参数")
public class PayProductConfigParam {

    @NotBlank(message = "{validation.field.product.notBlank}")
    @Schema(description = "产品编码")
    private String product;

    @NotBlank(message = "{validation.field.channel.notBlank}")
    @Schema(description = "通道编码")
    private String channel;

    @Schema(description = "生效环境: prod/sandbox")
    private String activeEnv;

    @Schema(description = "是否已配置参数")
    private boolean configured;

    @Schema(description = "备注")
    private String remark;
}
