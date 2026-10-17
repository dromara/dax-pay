package cn.daxpay.open.channel.kuaiqian.param.direct;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 快钱直连通道商户绑定创建参数
///
/// 创建时录入商户身份(快钱会员号/服务商子账户号/当面付商户与终端号),
/// 三份证书(商户 pfx/平台 cer/SSL p12)与密码由密钥配置单独维护。
@Data
@Accessors(chain = true)
@Schema(title = "快钱直连通道商户绑定创建参数")
public class KuaiqianDirectChannelMerchantCreateParam {

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
    @Schema(description = "快钱会员号/账户号(memberCode, 人民币网关商户账号 = memberCode + \"01\")")
    private String memberCode;

    @Schema(description = "服务商快钱子账户号(可空, 服务商形态: head.memberCode=子账户号)")
    private String vendorMemberCode;

    @NotBlank(message = "{validation.field.merchantId.notBlank}")
    @Schema(description = "当面付商户号(merchantId)")
    private String merchantId;

    @NotBlank(message = "{validation.field.terminalId.notBlank}")
    @Schema(description = "当面付终端号(terminalId)")
    private String terminalId;
}
