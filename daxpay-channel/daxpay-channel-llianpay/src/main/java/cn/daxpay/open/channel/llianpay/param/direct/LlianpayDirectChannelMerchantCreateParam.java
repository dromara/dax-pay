package cn.daxpay.open.channel.llianpay.param.direct;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 连连支付直连通道商户绑定创建参数
///
/// 创建时录入商户身份(连连商户编号与平台商/收款方业务参数),
/// 商户私钥与连连平台公钥由密钥配置单独维护。
@Data
@Accessors(chain = true)
@Schema(title = "连连支付直连通道商户绑定创建参数")
public class LlianpayDirectChannelMerchantCreateParam {

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
    @Schema(description = "连连商户编号(mch_id, 创建时录入不可修改)")
    private String mchId;

    @Schema(description = "二级商户号(可空, 平台商模式上送 sub_mchid)")
    private String subMchid;

    @Schema(description = "收款方用户ID(可空, 留空为商户自身收款)")
    private String payeeUid;

    @Schema(description = "业务属性(busi_type, 如 100001 虚拟商品购买; 缺省 100001)")
    private String busiType;
}
