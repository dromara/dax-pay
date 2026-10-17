package cn.daxpay.open.channel.youzan.param.direct;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 有赞直连通道商户绑定创建参数
///
/// 创建时录入商户身份(高汇通商户号), RSA 密钥三元组(商户私钥/有赞平台公钥/公钥唯一标识)由密钥配置单独维护。
@Data
@Accessors(chain = true)
@Schema(title = "有赞直连通道商户绑定创建参数")
public class YouzanDirectChannelMerchantCreateParam {

    @NotBlank(message = "{validation.field.mchNo.notBlank}")
    @Schema(description = "商户号")
    private String mchNo;

    @NotBlank(message = "{validation.field.channelMerchantName.notBlank}")
    @Schema(description = "通道商户名称")
    private String channelMerchantName;

    @NotBlank(message = "{validation.field.product.notBlank}")
    @Schema(description = "所属支付产品")
    private String product;

    @NotBlank(message = "{validation.field.youzanMerchantId.notBlank}")
    @Schema(description = "高汇通商户号(mch_id, 请求头 mchid 与 body mch_id 同值)")
    private String merchantId;
}
