package cn.daxpay.open.channel.mock.result.credential;

import cn.daxpay.open.platform.common.json.sensitive.SensitiveInfo;
import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # Mock 模拟服务商接入密钥(脱敏视图)
///
/// 两段 PEM 脱敏展示(@SensitiveInfo 用法与通道密钥配置一致);
/// 未保存过时仅 configured=false, 其余字段为空;
/// configured=商户私钥+平台公钥两者均非空(装配可用)。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "Mock模拟服务商接入密钥")
public class MockIsvCredentialResult extends BaseResult {

    @Schema(description = "服务商编号")
    private String isvNo;

    @Schema(description = "是否已配置接入密钥(=商户私钥/平台公钥均非空, 装配可用)")
    private Boolean configured;

    @Schema(description = "服务商接入网关地址(可空)")
    private String gatewayBaseUrl;

    @SensitiveInfo(front = 24, end = 24, compact = true)
    @Schema(description = "商户私钥(PEM PKCS#8 原文, 脱敏)")
    private String merchantPrivateKeyMasked;

    @SensitiveInfo(front = 24, end = 24, compact = true)
    @Schema(description = "平台公钥(PEM SPKI 原文, 脱敏)")
    private String platformPublicKeyMasked;

    @Schema(description = "最近一次保存时间(UTC)")
    private OffsetDateTime updateTime;
}
