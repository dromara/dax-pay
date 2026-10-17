package cn.daxpay.open.channel.yizhifubj.param.direct;

import cn.daxpay.open.platform.common.json.deserializer.CredentialKeepWhenBlankDeserializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import tools.jackson.databind.annotation.JsonDeserialize;

/// # 一直付直连密钥配置保存参数
///
/// 以 channelMchNo 定位记录, 仅更新可编辑密钥字段(服务端ID/报备序列号/商户 pfx 证书与密码/平台证书);
/// merchantId 为不可变身份字段, 创建时写入后不参与保存。
@Data
@Accessors(chain = true)
@Schema(title = "一直付直连密钥配置保存参数")
public class YizhifubjDirectKeyConfigParam {

    @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}")
    @Schema(description = "通道商户号")
    private String channelMchNo;

    @NotNull(message = "{validation.field.sandbox.notNull}")
    @Schema(description = "是否沙箱环境")
    private Boolean sandbox;

    @Schema(description = "服务端ID(partnerId, 可空)")
    private String partnerId;

    @Schema(description = "报备序列号(reportSerialNo, 可空)")
    private String reportSerialNo;

    @JsonDeserialize(using = CredentialKeepWhenBlankDeserializer.class)
    @Schema(description = "商户 pfx 证书内容(Base64, PKCS12)")
    private String certPfx;

    @JsonDeserialize(using = CredentialKeepWhenBlankDeserializer.class)
    @Schema(description = "商户证书密码")
    private String certPassword;

    @JsonDeserialize(using = CredentialKeepWhenBlankDeserializer.class)
    @Schema(description = "5upay 平台证书内容(Base64/DER, 可空, 空时用子应用内置默认证书)")
    private String platformCert;
}
