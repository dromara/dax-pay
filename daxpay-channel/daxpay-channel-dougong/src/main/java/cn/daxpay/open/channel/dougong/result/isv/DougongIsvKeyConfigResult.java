package cn.daxpay.open.channel.dougong.result.isv;

import cn.daxpay.open.platform.common.json.sensitive.SensitiveInfo;
import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 斗拱服务商密钥配置结果
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "斗拱服务商密钥配置")
public class DougongIsvKeyConfigResult extends BaseResult {

    @Schema(description = "产品编码")
    private String product;

    @Schema(description = "服务商系统ID(sysId)")
    private String sysId;

    @Schema(description = "产品号(productId)")
    private String productId;

    @SensitiveInfo(front = 12, end = 12)
    @Schema(description = "商户RSA私钥(加密存储)")
    private String privateKey;

    @SensitiveInfo(front = 12, end = 12)
    @Schema(description = "斗拱RSA公钥(加密存储)")
    private String dgPublicKey;
}
