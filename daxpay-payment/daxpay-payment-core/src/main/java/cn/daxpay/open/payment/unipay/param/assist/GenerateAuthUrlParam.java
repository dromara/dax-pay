package cn.daxpay.open.payment.unipay.param.assist;

import cn.daxpay.open.platform.core.enums.unipay.ChannelAuthTypeEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.ChannelEnum;
import cn.daxpay.open.payment.unipay.param.MerchantPaymentCommonParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 生成授权链接参数
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "生成授权链接参数")
public class GenerateAuthUrlParam extends MerchantPaymentCommonParam {

    /// 通道
    /// @see ChannelEnum
    @NotBlank(message = "{validation.field.channel.notBlank}")
    @Schema(description = "通道")
    private String channel;

    /// 认证类型, 如果通道支持多种类型的情况下, 不传默认为微信场景
    /// @see ChannelAuthTypeEnum
    @Schema(description = "认证类型")
    private String authType = ChannelAuthTypeEnum.WECHAT.getCode();

    /// 支付产品编码, 决定走哪个通道产品的认证策略
    /// 可选: 缺失时由 [cn.daxpay.open.payment.auth.ChannelAuthService] 从通道商户号(channelMchNo)反查
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum
    @Size(max = 32, message = "{validation.field.product.size}")
    @Schema(description = "支付产品编码")
    private String product;

    /// 指定认证使用的应用AppId, 优先级高于配置自动解析, 必须在系统中预先配置过
    @Size(max = 128, message = "{validation.field.opAppId.size}")
    @Schema(description = "指定认证应用AppId")
    private String opAppId;

    /// 来源回跳路径, 授权完成后前端回跳的目标路径, 会随会话码一起保存
    @Size(max = 200, message = "{validation.field.returnPath.size}")
    @Schema(description = "来源回跳路径")
    private String returnPath;

    /// 支付能力编码, 用于解析具体应用(公众号/小程序), 不同能力对应不同应用维度的openId
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.PayCapabilityEnum
    @Size(max = 32, message = "{validation.field.capability.size}")
    @Schema(description = "支付能力编码")
    private String capability;
}

