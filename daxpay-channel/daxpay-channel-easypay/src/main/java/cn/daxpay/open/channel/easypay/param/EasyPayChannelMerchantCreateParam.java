package cn.daxpay.open.channel.easypay.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 易支付通道商户创建参数
///
/// 创建时录入易支付平台地址与商户ID(pid), 签名密钥(商户私钥/平台公钥)
/// 不在此录入, 由密钥配置后置维护。
@Data
@Accessors(chain = true)
@Schema(title = "易支付通道商户创建参数")
public class EasyPayChannelMerchantCreateParam {

    /// 商户号
    @Schema(description = "商户号")
    @NotBlank(message = "{validation.field.mchNo.notBlank}")
    private String mchNo;

    /// 通道商户名称
    @Schema(description = "通道商户名称")
    @NotBlank(message = "{validation.field.channelMerchantName.notBlank}")
    private String channelMerchantName;

    /// 所属支付产品
    @Schema(description = "所属支付产品")
    @NotBlank(message = "{validation.field.product.notBlank}")
    private String product;

    /// 易支付平台网关地址
    @Schema(description = "易支付平台网关地址(如 https://pay.xxx.com)")
    @NotBlank(message = "{validation.field.serverUrl.notBlank}")
    private String serverUrl;

    /// 易支付商户ID(pid)
    @Schema(description = "易支付商户ID(pid, 上游平台分配)")
    @NotBlank(message = "{validation.field.partnerId.notBlank}")
    private String partnerId;
}
