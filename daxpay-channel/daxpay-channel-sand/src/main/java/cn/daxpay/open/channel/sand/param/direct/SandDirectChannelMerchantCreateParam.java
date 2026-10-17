package cn.daxpay.open.channel.sand.param.direct;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 杉德直连通道商户绑定创建参数
///
/// 创建时录入商户身份(杉德商户编号 mid + 市场产品编码),
/// 商户 pfx 私钥证书与杉德公钥证书由密钥配置单独维护。
@Data
@Accessors(chain = true)
@Schema(title = "杉德直连通道商户绑定创建参数")
public class SandDirectChannelMerchantCreateParam {

    @NotBlank(message = "{validation.field.mchNo.notBlank}")
    @Schema(description = "商户号")
    private String mchNo;

    @NotBlank(message = "{validation.field.channelMerchantName.notBlank}")
    @Schema(description = "通道商户名称")
    private String channelMerchantName;

    @NotBlank(message = "{validation.field.product.notBlank}")
    @Schema(description = "所属支付产品")
    private String product;

    @NotBlank(message = "{validation.field.mid.notBlank}")
    @Schema(description = "杉德商户编号(mid)")
    private String mid;

    @NotBlank(message = "{validation.field.marketProduct.notBlank}")
    @Schema(description = "市场产品编码(QZF 标准线上收款 / CSDB 企业杉德宝)")
    private String marketProduct;
}
