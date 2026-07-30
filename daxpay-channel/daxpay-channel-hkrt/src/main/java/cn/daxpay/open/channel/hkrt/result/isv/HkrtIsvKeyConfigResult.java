package cn.daxpay.open.channel.hkrt.result.isv;

import cn.daxpay.open.platform.common.json.sensitive.SensitiveInfo;
import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 海科融通服务商密钥配置
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "海科融通服务商密钥配置")
public class HkrtIsvKeyConfigResult extends BaseResult {

    @Schema(description = "产品编码")
    private String product;

    @Schema(description = "服务商编号")
    private String agentNo;

    @Schema(description = "接入机构标识")
    private String accessId;

    @SensitiveInfo(front = 6, end = 6)
    @Schema(description = "签名密钥(加密存储)")
    private String accessKey;

    @Schema(description = "是否沙箱环境")
    private Boolean sandbox;
}
