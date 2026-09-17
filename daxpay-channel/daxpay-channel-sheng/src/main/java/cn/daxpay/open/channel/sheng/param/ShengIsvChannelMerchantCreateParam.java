package cn.daxpay.open.channel.sheng.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 盛付通服务商通道商户创建参数
///
/// 创建时录入子商户身份(subMchId 必填, sdpAppId 可空),
/// 服务商签名密钥(私钥/盛付通公钥)不在此录入, 由产品级密钥配置全局统一维护。
@Data
@Accessors(chain = true)
@Schema(title = "盛付通服务商通道商户创建参数")
public class ShengIsvChannelMerchantCreateParam {

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

    /// 子商户号(服务商代子商户发起交易, 必填)
    @Schema(description = "子商户号(服务商代子商户发起交易)")
    @NotBlank(message = "{validation.field.subMchId.notBlank}")
    private String subMchId;

    /// 盛付通分配的子商户应用ID
    @Schema(description = "盛付通分配的子商户应用ID")
    private String sdpAppId;
}
