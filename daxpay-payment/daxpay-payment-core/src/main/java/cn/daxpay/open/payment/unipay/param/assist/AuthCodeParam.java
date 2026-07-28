package cn.daxpay.open.payment.unipay.param.assist;

import cn.daxpay.open.platform.core.enums.unipay.ChannelAuthTypeEnum;
import cn.daxpay.open.payment.unipay.param.MerchantPaymentCommonParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 通道认证参数
///
/// 认证回调场景(通过 /auth 端点对外暴露): 前端 OAuth 落地页用 authCode 换 openId/userId。
/// 应用凭证不挂在此类上, 由策略层自行从 session 恢复(微信读 wxAppScope/wxAppRefId 查密钥)。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "通道认证参数")
public class AuthCodeParam extends MerchantPaymentCommonParam {

    /// 认证类型, 如果通道支持多种类型的情况下, 参数必传
    /// @see ChannelAuthTypeEnum
    @Schema(description = "认证类型")
    private String authType;

    /// 三方通道 OAuth 授权码(换 openId/userId); 亦可直接写入已拿到的 openId
    @Schema(description = "授权码")
    @NotBlank(message = "{validation.field.oauthCode.notBlank}")
    private String authCode;

    /// 三方通道可以直接获取到这个值
    @Schema(description = "AccessToken")
    private String accessToken;

    /// App 标识，通过用户请求的 userAgent 中 appUpIdentifier 截取获得，
    /// 银联支付标识的格式为"UnionPay/<版本号> <App 标识>"例如 UnionPay/1.0 Cloudpay ，
    /// 其中 Cloudpay 即为 App 标识
    @Schema(description = "云闪付App标识")
    private String unionIdentifier;

    /// 用于查询Code值, 可以为空
    @Schema(description = "查询Code")
    private String queryCode;

    /// 认证会话码, H5授权重定向场景下由生成授权链接时下发, 回调后凭此恢复认证上下文
    @Size(max = 64, message = "{validation.field.authToken.size}")
    @Schema(description = "认证会话码")
    private String authToken;
}