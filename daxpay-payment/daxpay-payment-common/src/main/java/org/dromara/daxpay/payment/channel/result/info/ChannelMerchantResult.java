package org.dromara.daxpay.payment.channel.result.info;

import org.dromara.daxpay.platform.core.enums.channel.ChannelMerchantSourceEnum;
import org.dromara.daxpay.payment.common.result.MchTradeBaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 通道商户信息
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "通道商户信息")
public class ChannelMerchantResult extends MchTradeBaseResult {

    /// 申请单ID
    @Schema(description = "申请单ID")
    private Long applyId;

    /// 通道商户号
    @Schema(description = "通道商户号")
    private String channelMchNo;

    /// 商户全称
    @Schema(description = "商户全称")
    private String channelMerchantName;

    /// 所属支付产品
    @Schema(description = "所属支付产品")
    private String product;

    /// 通道商户创建来源
    /// @see ChannelMerchantSourceEnum
    @Schema(description = "创建来源")
    private String source;

    /// 是否启用
    @Schema(description = "是否启用")
    private Boolean enable;

}

