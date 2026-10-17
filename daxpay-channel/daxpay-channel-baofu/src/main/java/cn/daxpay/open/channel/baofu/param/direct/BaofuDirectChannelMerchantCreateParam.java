package cn.daxpay.open.channel.baofu.param.direct;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 宝付直连通道商户绑定创建参数
///
/// 创建时录入商户身份(宝付商户号/终端号),
/// 两份证书(商户 pfx/平台 cer)与密码及聚合交易商户号由密钥配置单独维护。
@Data
@Accessors(chain = true)
@Schema(title = "宝付直连通道商户绑定创建参数")
public class BaofuDirectChannelMerchantCreateParam {

    @NotBlank(message = "{validation.field.mchNo.notBlank}")
    @Schema(description = "商户号")
    private String mchNo;

    @NotBlank(message = "{validation.field.channelMerchantName.notBlank}")
    @Schema(description = "通道商户名称")
    private String channelMerchantName;

    @NotBlank(message = "{validation.field.product.notBlank}")
    @Schema(description = "所属支付产品")
    private String product;

    @NotBlank(message = "{validation.field.memberCode.notBlank}")
    @Schema(description = "宝付商户号(merId, 收款线)")
    private String merId;

    @NotBlank(message = "{validation.field.terminalId.notBlank}")
    @Schema(description = "宝付终端号(terId, 收款线)")
    private String terId;
}
