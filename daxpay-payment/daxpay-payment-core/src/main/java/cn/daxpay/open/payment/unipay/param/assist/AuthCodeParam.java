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
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "通道认证参数")
public class AuthCodeParam extends MerchantPaymentCommonParam {

    /// 通道(支付宝平台级认证可不传)
    @Schema(description = "通道")
    private String channel;

    /// 认证类型, 如果通道支持多种类型的情况下, 参数必传
    /// @see ChannelAuthTypeEnum
    @Schema(description = "认证类型")
    private String authType;

    /// 三方通道可以直接获取到OpeId, 可以写入这个至
    @Schema(description = "标识码/")
    @NotBlank(message = "{validation.field.authCode.notBlank}")
    private String authCode;

    /// 三方通道可以直接获取到这个值
    @Schema(description = "AccessToken")
    private String accessToken;

    /// App 标识，通过用户请求的 userAgent 中 appUpIdentifier 截取获得，
    /// 银联支付标识的格式为“UnionPay/<版本号> <App 标识>”例如 UnionPay/1.0 Cloudpay ，
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

    /// 支付产品编码, 小程序直连场景(无会话码)必传; H5会话码场景可不传(从会话恢复)
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum
    @Size(max = 32, message = "{validation.field.product.size}")
    @Schema(description = "支付产品编码")
    private String product;

    /// 支付能力编码, 用于解析具体应用, 小程序场景需要; 会话码场景从会话恢复
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.PayCapabilityEnum
    @Size(max = 32, message = "{validation.field.capability.size}")
    @Schema(description = "支付能力编码")
    private String capability;

    /// 指定认证使用的应用AppId, 会话码恢复上下文时可不传, 优先级高于配置自动解析
    @Size(max = 128, message = "{validation.field.channelAppId.size}")
    @Schema(description = "指定认证应用AppId")
    private String channelAppId;
}

