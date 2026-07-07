package cn.daxpay.open.channel.adapay.param.direct;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 汇付天下直连通道商户绑定创建参数
///
/// 创建时仅录入商户名称与所属产品, 汇付应用 ID/密钥由密钥配置单独维护,
/// 沙箱环境由支付产品配置(pay_md_product_config.activeEnv)决定。
@Data
@Accessors(chain = true)
@Schema(title = "汇付天下直连通道商户绑定创建参数")
public class AdapayDirectChannelMerchantCreateParam {

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
    @Schema(description = "汇付商户号")
    private String merchantNo;
}
