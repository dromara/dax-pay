package cn.daxpay.open.channel.sheng.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 盛付通通道商户创建参数
///
/// 创建时仅录入盛付通商户身份(shengMchId), 应用ID(sdpAppId)与签名密钥(商户私钥/盛付通公钥)
/// 均不在此录入, 由密钥配置后置维护。
@Data
@Accessors(chain = true)
@Schema(title = "盛付通通道商户创建参数")
public class ShengChannelMerchantCreateParam {

    /// 商户号
    @Schema(description = "商户号")
    @NotBlank(message = "{validation.field.mchNo.notBlank}")
    private String mchNo;

    /// 通道商户名称
    @Schema(description = "通道商户名称")
    @NotBlank(message = "{validation.field.channelMerchantName.notBlank}")
    private String channelMerchantName;

    /// 所属支付产品
    @Schema(description = "所属支付产品")
    @NotBlank(message = "{validation.field.product.notBlank}")
    private String product;

    /// 盛付通商户号(mchId)
    @Schema(description = "盛付通商户号(mchId)")
    @NotBlank(message = "{validation.field.shengMchId.notBlank}")
    private String shengMchId;
}
