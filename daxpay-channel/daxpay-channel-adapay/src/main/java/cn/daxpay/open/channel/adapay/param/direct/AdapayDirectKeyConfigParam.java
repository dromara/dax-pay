package cn.daxpay.open.channel.adapay.param.direct;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 汇付天下直连密钥配置保存参数
///
/// 以 channelMchNo(通道商户号) 作为唯一标识定位记录,
/// mchNo(平台商户号) 为不可变身份字段, 创建时写入后永不可改, 不参与保存。
@Data
@Accessors(chain = true)
@Schema(title = "汇付天下直连密钥配置保存参数")
public class AdapayDirectKeyConfigParam {

    @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}")
    @Schema(description = "通道商户号")
    private String channelMchNo;

    @Schema(description = "汇付支付应用 ID")
    private String adapayAppId;

    @Schema(description = "汇付 API Key")
    private String apiKey;

    @Schema(description = "商户 RSA 私钥(PKCS#8 Base64)")
    private String privateKey;

    @Schema(description = "汇付平台公钥(X509 Base64, 为空使用全局默认)")
    private String publicKey;
}
