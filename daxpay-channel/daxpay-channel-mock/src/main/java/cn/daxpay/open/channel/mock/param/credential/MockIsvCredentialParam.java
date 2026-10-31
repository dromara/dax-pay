package cn.daxpay.open.channel.mock.param.credential;

import cn.daxpay.open.platform.common.json.deserializer.CredentialKeepWhenBlankDeserializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;
import tools.jackson.databind.annotation.JsonDeserialize;

/// # Mock 模拟服务商接入密钥保存参数
///
/// 以 isvNo 定位记录按行 upsert; 密钥材料由运营从模拟网关控制台复制粘贴而来(不在本侧生成)。
/// 空值语义: 两段 PEM 留空表示不修改([CredentialKeepWhenBlankDeserializer] 反序列化时空串转 null,
/// 更新 copy 空值不覆盖); gatewayBaseUrl 明文可单独改。
/// 两段 PEM 各自独立: 可只换其中一段, 不做齐交约束(仅换私钥时需运营确认与网关侧当前密钥一致)。
@Data
@Accessors(chain = true)
@Schema(title = "Mock模拟服务商接入密钥保存参数")
public class MockIsvCredentialParam {

    @NotBlank(message = "{validation.field.isvNo.notBlank}")
    @Schema(description = "服务商编号")
    private String isvNo;

    @Schema(description = "服务商接入网关地址(可空)")
    private String gatewayBaseUrl;

    @JsonDeserialize(using = CredentialKeepWhenBlankDeserializer.class)
    @Schema(description = "商户私钥(PEM PKCS#8 原文, 留空表示不修改)")
    private String merchantPrivateKey;

    @JsonDeserialize(using = CredentialKeepWhenBlankDeserializer.class)
    @Schema(description = "平台公钥(PEM SPKI 原文, 留空表示不修改)")
    private String platformPublicKey;
}
