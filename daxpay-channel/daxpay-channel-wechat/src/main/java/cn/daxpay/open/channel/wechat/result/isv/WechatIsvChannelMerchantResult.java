package cn.daxpay.open.channel.wechat.result.isv;

import cn.daxpay.open.payment.common.result.MchTradeBaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 微信服务商通道商户绑定结果
///
/// 微信服务商通道商户绑定关系的返回结果对象，包含通道商户号、所属产品和特约商户号等信息。
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "微信服务商通道商户绑定结果")
public class WechatIsvChannelMerchantResult extends MchTradeBaseResult {

    @Schema(description = "通道商户号")
    private String channelMchNo;

    @Schema(description = "所属支付产品")
    private String product;

    @Schema(description = "微信特约商户号/二级商户号")
    private String subMchId;
}
