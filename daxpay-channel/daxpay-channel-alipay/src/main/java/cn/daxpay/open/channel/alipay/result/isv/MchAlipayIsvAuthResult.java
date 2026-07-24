package cn.daxpay.open.channel.alipay.result.isv;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 支付宝服务商代运营授权列表项（商户端，脱敏）
///
/// 供商户端「业务配置 > 支付宝ISV授权」使用。
/// 不返回应用授权令牌（对照 [AlipayIsvChannelMerchantResult#getAppAuthToken]），
/// 仅暴露是否已授权，避免敏感凭据下发到商户端。
@Data
@Accessors(chain = true)
@Schema(title = "支付宝服务商代运营授权列表项(商户端)")
public class MchAlipayIsvAuthResult {

    /// 通道商户号
    @Schema(description = "通道商户号")
    private String channelMchNo;

    /// 所属支付产品
    @Schema(description = "所属支付产品")
    private String product;

    /// 子商户支付宝识别码(2088开头的16位数字)
    @Schema(description = "子商户支付宝识别码")
    private String alipayUserId;

    /// 是否已授权(appAuthToken 是否已绑定)
    @Schema(description = "是否已授权")
    private Boolean authorized;
}
