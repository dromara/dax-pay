package cn.daxpay.open.channel.wechat.param.isv;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 微信服务商密钥配置保存参数
///
@Data
@Accessors(chain = true)
@Schema(title = "微信服务商密钥配置保存参数")
public class WechatIsvKeyConfigParam {

    @NotBlank(message = "{validation.field.product.notBlank}")
    @Schema(description = "产品编码")
    private String product;

    @NotBlank(message = "{validation.field.wxMchId.notBlank}")
    @Schema(description = "微信服务商商户号")
    private String wxMchId;

    @Schema(description = "API V3密钥(加密存储)")
    private String apiKeyV3;

    @Schema(description = "支付公钥(加密存储)")
    private String publicKey;

    @Schema(description = "支付公钥ID")
    private String publicKeyId;

    @Schema(description = "apiclient_key证书(加密存储)")
    private String privateKey;

    @Schema(description = "apiclient_cert证书(加密存储)")
    private String privateCert;

    @Schema(description = "证书序列号")
    private String certSerialNo;
}
