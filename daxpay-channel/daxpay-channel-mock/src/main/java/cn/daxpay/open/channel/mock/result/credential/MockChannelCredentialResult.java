package cn.daxpay.open.channel.mock.result.credential;

import cn.daxpay.open.platform.common.json.sensitive.SensitiveInfo;
import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # Mock 模拟通道网关签名密钥配置
///
/// 敏感字段脱敏展示(前端回显占位, 留空表示不修改语义)。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "Mock模拟通道网关签名密钥配置")
public class MockChannelCredentialResult extends BaseResult {

    @SensitiveInfo(front = 24, end = 24, compact = true)
    @Schema(description = "商户私钥(PEM PKCS#8 原文, 脱敏)")
    private String merchantPrivateKey;

    @SensitiveInfo(front = 24, end = 24, compact = true)
    @Schema(description = "平台公钥(PEM SPKI 原文, 脱敏)")
    private String platformPublicKey;

    @Schema(description = "是否已配置完整(两密钥均非空)")
    private Boolean configured;
}
