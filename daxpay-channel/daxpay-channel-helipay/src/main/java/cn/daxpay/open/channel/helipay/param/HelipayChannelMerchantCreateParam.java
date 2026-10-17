package cn.daxpay.open.channel.helipay.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 合利宝通道商户创建参数
///
/// 创建为纯建档动作, 仅录入通道商户名称; 合利宝对接配置(商户编号/签名密钥/场景AppId)
/// 全部由密钥配置后置维护, 发起支付前由密钥完整性校验兜底。
@Data
@Accessors(chain = true)
@Schema(title = "合利宝通道商户创建参数")
public class HelipayChannelMerchantCreateParam {

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
}
