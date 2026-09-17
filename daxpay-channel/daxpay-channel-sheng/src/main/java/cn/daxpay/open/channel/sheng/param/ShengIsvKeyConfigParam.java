package cn.daxpay.open.channel.sheng.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 盛付通服务商密钥配置保存参数
///
/// 以 product(产品编码) 作为唯一标识定位记录, 服务商凭证产品级全局一份;
/// 密钥字段为空时保留原值(不覆盖), 前端回显的脱敏值不上送。
@Data
@Accessors(chain = true)
@Schema(title = "盛付通服务商密钥配置保存参数")
public class ShengIsvKeyConfigParam {

    @NotBlank(message = "{validation.field.product.notBlank}")
    @Schema(description = "产品编码")
    private String product;

    @Schema(description = "服务商盛付通商户号(mchId)")
    private String shengMchId;

    @Schema(description = "服务商RSA私钥(PKCS8)")
    private String merchantPrivateKey;

    @Schema(description = "盛付通验签公钥")
    private String shengpayPublicKey;
}
