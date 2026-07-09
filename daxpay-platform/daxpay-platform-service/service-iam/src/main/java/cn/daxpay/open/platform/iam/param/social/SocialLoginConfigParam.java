package cn.daxpay.open.platform.iam.param.social;

import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 第三方平台登录配置参数
///
@Data
@Accessors(chain = true)
@Schema(title = "第三方平台登录配置参数")
public class SocialLoginConfigParam {

    @Schema(description = "平台编码")
    @NotBlank(message = "{validation.field.source.notBlank}")
    private String source;

    /// 客户端ID(标准 OAuth 平台必填; 平台级跳转型如支付宝不传, 由 Service 分支校验)
    @Schema(description = "客户端ID")
    private String clientId;

    /// 客户端密钥(新增必填, 编辑未修改不传该字段, 修改传新值)
    /// 配合前端 diffForm 比对: 未修改时字段为 undefined, JSON 序列化时被忽略,
    /// 后端默认 NOT_NULL 策略下 null 不参与 UPDATE, 保持数据库原密文不变.
    @Schema(description = "客户端密钥(编辑未修改不传, 修改传新值)")
    private String clientSecret;

    /// 平台特有配置(如企业微信 agentId)
    @Schema(description = "平台特有配置(如企业微信 agentId)")
    private Map<String, String> extra;

    @Schema(description = "是否启用")
    private Boolean enabled;
}
