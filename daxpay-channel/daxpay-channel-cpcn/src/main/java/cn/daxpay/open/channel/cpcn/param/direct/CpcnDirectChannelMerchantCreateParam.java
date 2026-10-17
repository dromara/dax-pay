package cn.daxpay.open.channel.cpcn.param.direct;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 中金直连通道商户绑定创建参数
///
/// 创建时录入商户身份(中金机构编号), 网关地址/指定分账子商户号与两份证书
/// (商户 pfx / 中金平台证书)由密钥配置单独维护。
@Data
@Accessors(chain = true)
@Schema(title = "中金直连通道商户绑定创建参数")
public class CpcnDirectChannelMerchantCreateParam {

    @NotBlank(message = "{validation.field.mchNo.notBlank}")
    @Schema(description = "商户号")
    private String mchNo;

    @NotBlank(message = "{validation.field.channelMerchantName.notBlank}")
    @Schema(description = "通道商户名称")
    private String channelMerchantName;

    @NotBlank(message = "{validation.field.product.notBlank}")
    @Schema(description = "所属支付产品")
    private String product;

    @NotBlank(message = "{validation.field.institutionId.notBlank}")
    @Schema(description = "中金机构编号(InstitutionID)")
    private String institutionId;
}
