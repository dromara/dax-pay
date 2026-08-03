package cn.daxpay.open.channel.union.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 云闪付通道商户绑定创建参数
///
/// 创建时仅录入银联商户号(merId)与商户名称,
/// RSA2 三证书(私钥/中级/根)由密钥配置单独维护,
/// 沙箱环境由支付产品配置(pay_md_product_config.activeEnv)决定。
@Data
@Accessors(chain = true)
@Schema(title = "云闪付通道商户绑定创建参数")
public class UnionChannelMerchantCreateParam {

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
    @Schema(description = "银联商户号(merId)")
    private String merId;
}
