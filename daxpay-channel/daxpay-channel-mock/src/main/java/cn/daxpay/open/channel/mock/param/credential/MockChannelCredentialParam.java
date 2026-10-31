package cn.daxpay.open.channel.mock.param.credential;

import cn.daxpay.open.platform.common.json.deserializer.CredentialKeepWhenBlankDeserializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import tools.jackson.databind.annotation.JsonDeserialize;

/// # Mock 模拟通道网关签名密钥配置保存参数
///
/// 空值语义: 敏感字段留空表示不修改([CredentialKeepWhenBlankDeserializer] 反序列化时空串转 null,
/// 更新 copy 空值不覆盖)。
@Data
@Accessors(chain = true)
@Schema(title = "Mock模拟通道网关签名密钥配置保存参数")
public class MockChannelCredentialParam {

    @JsonDeserialize(using = CredentialKeepWhenBlankDeserializer.class)
    @Schema(description = "商户私钥(PEM PKCS#8 原文, 留空表示不修改)")
    private String merchantPrivateKey;

    @JsonDeserialize(using = CredentialKeepWhenBlankDeserializer.class)
    @Schema(description = "平台公钥(PEM SPKI 原文, 留空表示不修改)")
    private String platformPublicKey;
}
