package cn.daxpay.open.channel.yisheng.param.direct;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 易生直连通道商户绑定创建参数
///
/// 创建时录入商户身份(合作伙伴ID/商户编号), RSA 密钥对(商户私钥/易生平台公钥)由密钥配置单独维护。
@Data
@Accessors(chain = true)
@Schema(title = "易生直连通道商户绑定创建参数")
public class YishengDirectChannelMerchantCreateParam {

    @NotBlank(message = "{validation.field.mchNo.notBlank}")
    @Schema(description = "商户号")
    private String mchNo;

    @NotBlank(message = "{validation.field.channelMerchantName.notBlank}")
    @Schema(description = "通道商户名称")
    private String channelMerchantName;

    @NotBlank(message = "{validation.field.product.notBlank}")
    @Schema(description = "所属支付产品")
    private String product;

    @NotBlank(message = "{validation.field.yishengPartner.notBlank}")
    @Schema(description = "合作伙伴ID(reqId, 网关公共参数 partner)")
    private String partner;

    @NotBlank(message = "{validation.field.yishengMerchantId.notBlank}")
    @Schema(description = "易生商户编号(merchant_id)")
    private String merchantId;
}
