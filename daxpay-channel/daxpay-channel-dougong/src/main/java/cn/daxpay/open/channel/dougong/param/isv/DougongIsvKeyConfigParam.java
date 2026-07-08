package cn.daxpay.open.channel.dougong.param.isv;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 斗拱服务商密钥配置保存参数
@Data
@Accessors(chain = true)
@Schema(title = "斗拱服务商密钥配置保存参数")
public class DougongIsvKeyConfigParam {

    @NotBlank(message = "{validation.field.product.notBlank}")
    @Schema(description = "产品编码")
    private String product;

    @NotBlank(message = "{validation.field.sysId.notBlank}")
    @Schema(description = "服务商系统ID(sysId)")
    private String sysId;

    @NotBlank(message = "{validation.field.productId.notBlank}")
    @Schema(description = "产品号(productId)")
    private String productId;

    @Schema(description = "商户RSA私钥(加密存储)")
    private String privateKey;

    @Schema(description = "斗拱RSA公钥(加密存储)")
    private String dgPublicKey;
}
