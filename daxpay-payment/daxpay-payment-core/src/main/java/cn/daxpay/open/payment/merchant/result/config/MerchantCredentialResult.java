package cn.daxpay.open.payment.merchant.result.config;

import cn.daxpay.open.platform.common.json.sensitive.SensitiveInfo;
import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 商户API配置结果
///
/// 配置状态字段(credentialConfigured/publicKeyConfigured/secretKeyConfigured)由
/// [cn.daxpay.open.payment.merchant.service.config.MerchantCredentialService] 组装时按统一口径填充。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "商户API配置结果")
public class MerchantCredentialResult extends BaseResult {

    /// 商户公钥（可公开，不脱敏）
    @Schema(description = "商户公钥")
    private String publicKey;

    /// 平台公钥
    @Schema(description = "平台公钥")
    private String platformPublicKey;

    /// 通信密钥
    @Schema(description = "通信密钥")
    @SensitiveInfo(front = 6, end = 6)
    private String secretKey;

    /// 凭证是否已配置完整
    ///
    /// 商户公钥与通信密钥均非空才为 true,
    /// 口径见 [cn.daxpay.open.payment.merchant.service.config.MerchantCredentialService#isConfigured]
    @Schema(description = "凭证是否已配置完整")
    private boolean credentialConfigured;

    /// 商户公钥是否已配置
    @Schema(description = "商户公钥是否已配置")
    private boolean publicKeyConfigured;

    /// 通信密钥是否已配置
    @Schema(description = "通信密钥是否已配置")
    private boolean secretKeyConfigured;

    /// 最后修改时间(UTC)
    ///
    /// 由 Convert 同名映射实体字段; 查询无记录时会自动建空行, 创建时间不代表配置时间, 故以此为准
    @Schema(description = "最后修改时间(UTC)")
    private OffsetDateTime lastModifiedTime;
}
