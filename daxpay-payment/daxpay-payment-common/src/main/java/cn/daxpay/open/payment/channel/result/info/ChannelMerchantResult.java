package cn.daxpay.open.payment.channel.result.info;

import cn.daxpay.open.platform.core.enums.channel.ChannelMerchantSourceEnum;
import cn.daxpay.open.payment.common.result.MchTradeBaseResult;
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

    /// 当前生效环境(来自支付产品配置, 商户只读): prod/sandbox
    @Schema(description = "当前生效环境: prod/sandbox")
    private String activeEnv;

    /// 是否支持沙箱环境(决定前端是否展示环境标签)
    @Schema(description = "是否支持沙箱环境")
    private Boolean sandboxSupport;

}

