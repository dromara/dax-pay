package cn.daxpay.open.platform.system.result.config.security;

import java.util.List;

import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 通行密钥配置结果
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "通行密钥配置结果")
public class PlatformWebAuthnConfigResult extends BaseResult {

    @Schema(description = "是否启用通行密钥认证")
    private Boolean enabled;

    @Schema(description = "依赖方ID(域名)")
    private String rpId;

    @Schema(description = "依赖方显示名称")
    private String rpName;

    @Schema(description = "允许的调用来源列表(完整 origin)")
    private List<String> origins;
}
