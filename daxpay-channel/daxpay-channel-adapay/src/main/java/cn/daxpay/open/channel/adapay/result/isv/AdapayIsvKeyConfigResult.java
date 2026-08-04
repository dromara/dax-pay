package cn.daxpay.open.channel.adapay.result.isv;

import cn.daxpay.open.platform.common.json.sensitive.SensitiveInfo;
import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # Adapay 服务商密钥配置结果
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "Adapay 服务商密钥配置")
public class AdapayIsvKeyConfigResult extends BaseResult {

    /// 服务商号(平台在汇付的服务商/主体编号)
    @Schema(description = "服务商号")
    private String isvNo;

    /// Adapay 交易密钥(脱敏返回)
    @SensitiveInfo(front = 6, end = 6)
    @Schema(description = "Adapay 交易密钥(脱敏返回)")
    private String apiKey;

    /// 商户 RSA 私钥(脱敏返回)
    @SensitiveInfo(front = 24, end = 24)
    @Schema(description = "商户RSA私钥(脱敏返回)")
    private String privateKey;

    /// Adapay 平台公钥(脱敏返回)
    @SensitiveInfo(front = 24, end = 24)
    @Schema(description = "Adapay 平台公钥(脱敏返回)")
    private String publicKey;

    /// 是否沙箱环境
    @Schema(description = "是否沙箱环境")
    private Boolean sandbox;
}
