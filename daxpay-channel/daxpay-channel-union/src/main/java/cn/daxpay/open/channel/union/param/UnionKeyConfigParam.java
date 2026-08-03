package cn.daxpay.open.channel.union.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 云闪付密钥配置保存参数
///
/// 以 channelMchNo(通道商户号) 作为唯一标识定位记录,
/// mchNo/merId(银联商户号) 为不可变身份字段, 创建时写入后永不可改, 不参与保存。
@Data
@Accessors(chain = true)
@Schema(title = "云闪付密钥配置保存参数")
public class UnionKeyConfigParam {

    @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}")
    @Schema(description = "通道商户号")
    private String channelMchNo;

    @Schema(description = "银联商户号(merId)")
    private String merId;

    @Schema(description = "签名类型(银联 ACP 固定 RSA2)")
    private String signType;

    @Schema(description = "是否证书签名")
    private Boolean certSign;

    @Schema(description = "应用私钥证书(Base64 PKCS12)")
    private String keyPrivateCert;

    @Schema(description = "私钥证书密码")
    private String keyPrivateCertPwd;

    @Schema(description = "中级证书(Base64 X.509)")
    private String acpMiddleCert;

    @Schema(description = "根证书(Base64 X.509)")
    private String acpRootCert;

    @NotNull(message = "{validation.field.sandbox.notNull}")
    @Schema(description = "是否沙箱环境")
    private Boolean sandbox;
}
