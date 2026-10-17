package cn.daxpay.open.channel.fubei.param.direct;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 付呗直连通道商户绑定创建参数
///
/// 创建时录入商户身份(开放平台应用ID/商户ID/门店ID),
/// 接口密钥(app_secret)由密钥配置单独维护。
@Data
@Accessors(chain = true)
@Schema(title = "付呗直连通道商户绑定创建参数")
public class FubeiDirectChannelMerchantCreateParam {

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
    @Schema(description = "付呗开放平台应用ID(app_id)")
    private String appId;

    @Schema(description = "付呗商户ID(merchant_id, 服务商形态时必传, 可空)")
    private String merchantId;

    @NotBlank(message = "{validation.field.storeNo.notBlank}")
    @Schema(description = "付呗门店ID(store_id)")
    private String storeId;
}
