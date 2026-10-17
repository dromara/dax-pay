package cn.daxpay.open.channel.shouqianba.param.direct;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 收钱吧直连通道商户绑定创建参数
///
/// 创建时录入终端号(terminal_sn), 终端密钥由密钥配置单独维护(签到重置后可更新)。
@Data
@Accessors(chain = true)
@Schema(title = "收钱吧直连通道商户绑定创建参数")
public class ShouqianbaDirectChannelMerchantCreateParam {

    @NotBlank(message = "{validation.field.mchNo.notBlank}")
    @Schema(description = "商户号")
    private String mchNo;

    @NotBlank(message = "{validation.field.channelMerchantName.notBlank}")
    @Schema(description = "通道商户名称")
    private String channelMerchantName;

    @NotBlank(message = "{validation.field.product.notBlank}")
    @Schema(description = "所属支付产品")
    private String product;

    @NotBlank(message = "{validation.field.terminalSn.notBlank}")
    @Schema(description = "收钱吧终端号(terminal_sn, 终端激活后获得, 创建后不可修改)")
    private String terminalSn;

    @Schema(description = "操作员(收钱吧要求上送, 缺省 daxpay)")
    private String operator;
}
