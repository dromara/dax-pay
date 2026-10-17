package cn.daxpay.open.channel.xingyi.param.direct;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 星驿付直连通道商户绑定创建参数
///
/// 创建时录入商户身份(机构号/星驿付商户号),
/// 商户 RSA 公钥由密钥配置单独维护。
@Data
@Accessors(chain = true)
@Schema(title = "星驿付直连通道商户绑定创建参数")
public class XingyiDirectChannelMerchantCreateParam {

    @NotBlank(message = "{validation.field.mchNo.notBlank}")
    @Schema(description = "商户号")
    private String mchNo;

    @NotBlank(message = "{validation.field.channelMerchantName.notBlank}")
    @Schema(description = "通道商户名称")
    private String channelMerchantName;

    @NotBlank(message = "{validation.field.product.notBlank}")
    @Schema(description = "所属支付产品")
    private String product;

    @NotBlank(message = "{validation.field.agetId.notBlank}")
    @Schema(description = "机构号(agetId)")
    private String agetId;

    @NotBlank(message = "{validation.field.custId.notBlank}")
    @Schema(description = "星驿付商户号(custId)")
    private String custId;
}
