package cn.daxpay.open.channel.epaylinks.param.direct;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 易票联直连通道商户绑定创建参数
///
/// 创建时录入商户身份(易票联商户编号/上游商户号),
/// 密钥三元组(证书序列号+商户私钥+平台公钥证书)由密钥配置单独维护。
@Data
@Accessors(chain = true)
@Schema(title = "易票联直连通道商户绑定创建参数")
public class EpaylinksDirectChannelMerchantCreateParam {

    @NotBlank(message = "{validation.field.mchNo.notBlank}")
    @Schema(description = "商户号")
    private String mchNo;

    @NotBlank(message = "{validation.field.channelMerchantName.notBlank}")
    @Schema(description = "通道商户名称")
    private String channelMerchantName;

    @NotBlank(message = "{validation.field.product.notBlank}")
    @Schema(description = "所属支付产品")
    private String product;

    @NotBlank(message = "{validation.field.mchId.notBlank}")
    @Schema(description = "易票联商户编号(customerCode)")
    private String customerCode;

    @Schema(description = "上游商户号(可空; 设置后下单上送 channelMchtNo 指定受理商户)")
    private String channelMchtNo;
}
