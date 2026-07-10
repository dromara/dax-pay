package cn.daxpay.open.channel.wechat.result.isv;

import cn.daxpay.open.platform.common.json.sensitive.SensitiveInfo;
import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 微信服务商通道商户应用授权认证配置
///
/// 微信服务商通道商户应用授权认证配置的返回结果对象,含商户号、通道商户号、应用密钥(脱敏)。
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "微信服务商通道商户应用授权认证配置")
public class WechatIsvMchAppAuthConfigResult extends BaseResult {

    @Schema(description = "商户号")
    private String mchNo;

    @Schema(description = "通道商户号")
    private String channelMchNo;

    @Schema(description = "关联应用ID")
    private Long wechatIsvMchAppId;

    // 应用密钥(序列化时脱敏)
    @SensitiveInfo
    @Schema(description = "应用密钥(已脱敏)")
    private String appSecret;
}
