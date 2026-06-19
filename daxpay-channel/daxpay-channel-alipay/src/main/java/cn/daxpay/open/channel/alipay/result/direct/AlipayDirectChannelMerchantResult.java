package cn.daxpay.open.channel.alipay.result.direct;

import cn.daxpay.open.payment.common.result.MchTradeBaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 支付宝直连通道商户绑定结果
///
/// 支付宝直连通道商户绑定关系的返回结果对象，包含通道商户号、所属产品和支付宝商家识别码等信息。
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "支付宝直连通道商户绑定结果")
public class AlipayDirectChannelMerchantResult extends MchTradeBaseResult {

    @Schema(description = "通道商户号")
    private String channelMchNo;

    @Schema(description = "所属支付产品")
    private String product;

    @Schema(description = "支付宝商家唯一识别码(2088开头)")
    private String alipayUserId;
}
