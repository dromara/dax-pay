package cn.daxpay.open.channel.yizhifubj.param.direct;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 一直付直连通道商户绑定创建参数
///
/// 创建时录入商户身份(一直付商户编号), 服务端ID/报备序列号/商户 pfx 证书由密钥配置单独维护。
@Data
@Accessors(chain = true)
@Schema(title = "一直付直连通道商户绑定创建参数")
public class YizhifubjDirectChannelMerchantCreateParam {

    @NotBlank(message = "{validation.field.mchNo.notBlank}")
    @Schema(description = "商户号")
    private String mchNo;

    @NotBlank(message = "{validation.field.channelMerchantName.notBlank}")
    @Schema(description = "通道商户名称")
    private String channelMerchantName;

    @NotBlank(message = "{validation.field.product.notBlank}")
    @Schema(description = "所属支付产品")
    private String product;

    @NotBlank(message = "{validation.field.yizhifubjMerchantId.notBlank}")
    @Schema(description = "一直付商户编号(merchantId)")
    private String merchantId;
}
