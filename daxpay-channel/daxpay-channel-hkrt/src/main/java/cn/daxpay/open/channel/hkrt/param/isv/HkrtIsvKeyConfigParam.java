package cn.daxpay.open.channel.hkrt.param.isv;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 海科融通服务商密钥配置保存参数
///
@Data
@Accessors(chain = true)
@Schema(title = "海科融通服务商密钥配置保存参数")
public class HkrtIsvKeyConfigParam {

    @NotBlank(message = "{validation.field.product.notBlank}")
    @Schema(description = "产品编码")
    private String product;

    @NotBlank(message = "{validation.field.agentNo.notBlank}")
    @Schema(description = "服务商编号")
    private String agentNo;

    @NotBlank(message = "{validation.field.accessId.notBlank}")
    @Schema(description = "接入机构标识")
    private String accessId;

    @Schema(description = "签名密钥(加密存储)")
    private String accessKey;
}
