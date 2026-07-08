package cn.daxpay.open.channel.yeepay.param.direct;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 易宝直连通道商户绑定创建参数
///
/// 创建时仅录入商户身份(merchantNo/yopIsvNo)与商户名称,
/// 密钥(appKey/privateKey/yopPublicKey/wxAppId/wxAppSecret)由密钥配置单独维护,
/// 沙箱环境由支付产品配置(pay_md_product_config.activeEnv)决定。
@Data
@Accessors(chain = true)
@Schema(title = "易宝直连通道商户绑定创建参数")
public class YeepayDirectChannelMerchantCreateParam {

    @NotBlank(message = "{validation.field.mchNo.notBlank}")
    @Schema(description = "商户号")
    private String mchNo;

    @NotBlank(message = "{validation.field.channelMerchantName.notBlank}")
    @Schema(description = "通道商户名称")
    private String channelMerchantName;

    @NotBlank(message = "{validation.field.product.notBlank}")
    @Schema(description = "所属支付产品")
    private String product;

    @NotBlank(message = "{validation.field.merchantNo.notBlank}")
    @Schema(description = "易宝商户号(merchantNo)")
    private String merchantNo;

    @NotBlank(message = "{validation.field.yopIsvNo.notBlank}")
    @Schema(description = "易宝服务商商编(yopIsvNo / parentMerchantNo)")
    private String yopIsvNo;
}
