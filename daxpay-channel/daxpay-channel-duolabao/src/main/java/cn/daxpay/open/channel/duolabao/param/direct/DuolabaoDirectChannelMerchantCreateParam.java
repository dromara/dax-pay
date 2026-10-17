package cn.daxpay.open.channel.duolabao.param.direct;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 哆啦宝直连通道商户绑定创建参数
///
/// 创建时录入商户身份(商户编号/店铺编号/代理商编号),
/// accessKey/secretKey 密钥对由密钥配置单独维护。
@Data
@Accessors(chain = true)
@Schema(title = "哆啦宝直连通道商户绑定创建参数")
public class DuolabaoDirectChannelMerchantCreateParam {

    @NotBlank(message = "{validation.field.mchNo.notBlank}")
    @Schema(description = "商户号")
    private String mchNo;

    @NotBlank(message = "{validation.field.channelMerchantName.notBlank}")
    @Schema(description = "通道商户名称")
    private String channelMerchantName;

    @NotBlank(message = "{validation.field.product.notBlank}")
    @Schema(description = "所属支付产品")
    private String product;

    @NotBlank(message = "{validation.field.customerNum.notBlank}")
    @Schema(description = "商户编号(customerNum)")
    private String customerNum;

    @Schema(description = "店铺编号(shopNum, 可空)")
    private String shopNum;

    @Schema(description = "代理商编号(agentNum, 可空, 非代理商不需要填写)")
    private String agentNum;
}
