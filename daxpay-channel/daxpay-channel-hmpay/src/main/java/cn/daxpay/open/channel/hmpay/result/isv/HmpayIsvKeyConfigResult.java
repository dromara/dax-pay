package cn.daxpay.open.channel.hmpay.result.isv;

import cn.daxpay.open.platform.common.json.sensitive.SensitiveInfo;
import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 河马付服务商密钥配置结果
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "河马付服务商密钥配置")
public class HmpayIsvKeyConfigResult extends BaseResult {

    @Schema(description = "产品编码")
    private String product;

    @Schema(description = "杉德代理号(sandAppId)")
    private String sandAppId;

    @SensitiveInfo(front = 12, end = 12)
    @Schema(description = "商户RSA私钥(加密存储)")
    private String privateKey;

    @SensitiveInfo(front = 12, end = 12)
    @Schema(description = "杉德RSA公钥(加密存储)")
    private String publicKey;

    @Schema(description = "是否沙箱环境")
    private Boolean sandbox;
}
