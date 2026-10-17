package cn.daxpay.open.channel.jlpay.param.direct;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 嘉联直连通道商户绑定创建参数
///
/// 创建时录入商户身份(嘉联应用 appid / 商户号 / 终端号),
/// SM2 密钥对(商户私钥 + 平台公钥)由密钥配置单独维护。
@Data
@Accessors(chain = true)
@Schema(title = "嘉联直连通道商户绑定创建参数")
public class JlpayDirectChannelMerchantCreateParam {

    @NotBlank(message = "{validation.field.mchNo.notBlank}")
    @Schema(description = "商户号")
    private String mchNo;

    @NotBlank(message = "{validation.field.channelMerchantName.notBlank}")
    @Schema(description = "通道商户名称")
    private String channelMerchantName;

    @NotBlank(message = "{validation.field.product.notBlank}")
    @Schema(description = "所属支付产品")
    private String product;

    @NotBlank(message = "{validation.field.appId.notBlank}")
    @Schema(description = "嘉联开放平台应用 appid(请求头 x-jlpay-appid)")
    private String appId;

    @NotBlank(message = "{validation.field.channelMchNo.notBlank}")
    @Schema(description = "嘉联商户号(mch_id)")
    private String mchId;

    @NotBlank(message = "{validation.field.termNo.notBlank}")
    @Schema(description = "嘉联终端号(term_no)")
    private String termNo;
}
