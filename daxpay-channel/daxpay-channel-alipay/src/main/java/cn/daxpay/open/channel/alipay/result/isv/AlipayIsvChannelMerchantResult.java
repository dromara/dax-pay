package cn.daxpay.open.channel.alipay.result.isv;

import cn.daxpay.open.payment.common.result.MchBaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 支付宝服务商通道商户绑定结果
///
/// 支付宝服务商通道商户绑定关系的返回结果对象，包含通道商户号、所属产品、关联服务商应用ID等信息。
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "支付宝服务商通道商户绑定结果")
public class AlipayIsvChannelMerchantResult extends MchBaseResult {

    @Schema(description = "通道商户号")
    private String channelMchNo;

    @Schema(description = "所属支付产品")
    private String product;

    @Schema(description = "关联服务商应用ID(系统主键)")
    private Long isvAppId;

    @Schema(description = "子商户支付宝识别码(2088开头)")
    private String alipayUserId;

    @Schema(description = "应用授权令牌")
    private String appAuthToken;
}
