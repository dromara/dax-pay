package cn.daxpay.open.channel.vbill.result.isv;

import cn.daxpay.open.platform.common.json.sensitive.SensitiveInfo;
import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 随行付服务商密钥配置结果
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "随行付服务商密钥配置")
public class VbillIsvKeyConfigResult extends BaseResult {

    @Schema(description = "产品编码")
    private String product;

    @Schema(description = "天阙合作机构ID(orgId)")
    private String orgId;

    @SensitiveInfo(front = 24, end = 24)
    @Schema(description = "天阙RSA公钥(加密存储)")
    private String publicKey;

    @SensitiveInfo(front = 24, end = 24)
    @Schema(description = "商户RSA私钥(加密存储)")
    private String privateKey;

    @Schema(description = "是否沙箱环境")
    private Boolean sandbox;
}
