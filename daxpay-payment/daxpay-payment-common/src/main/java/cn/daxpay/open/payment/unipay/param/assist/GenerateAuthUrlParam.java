package cn.daxpay.open.payment.unipay.param.assist;

import cn.daxpay.open.platform.core.enums.unipay.ChannelAuthTypeEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.ChannelEnum;
import cn.daxpay.open.payment.unipay.param.MerchantPaymentCommonParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
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

    /// 自定义授权认证路径需要为网关前端系统中的路径
    /// 最后将形成一个这样格式的地址: http://{配置的域名}/{自定义授权认证路径和相关路径参数}
    /// 配置的域名优先级: 通道配置 》 平台配置
    @Schema(description = "授权认证路径")
    private String authPath;
}

