package cn.daxpay.open.payment.merchant.result.channel;

import cn.daxpay.open.payment.common.result.MchBaseResult;
import cn.daxpay.open.platform.core.enums.channel.ChannelMerchantSourceEnum;
import cn.daxpay.open.payment.merchant.entity.info.MerchantInfo;
import cn.daxpay.open.platform.core.annotation.Trans;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

/// # 通道商户信息
///
@EqualsAndHashCode(callSuper = true)
@Data
@FieldNameConstants
@Accessors(chain = true)
@Schema(title = "通道商户信息")
public class ChannelMerchantResult extends MchBaseResult {

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

    /// 是否沙箱(创建时按当时产品 activeEnv 固化写入, 之后不随产品切换改变)
    @Schema(description = "是否沙箱(创建时固化的环境标识)")
    private boolean sandbox;

    /// 是否支持沙箱环境(所属产品的能力标志, 决定前端是否展示环境标签)
    @Schema(description = "是否支持沙箱环境")
    private Boolean sandboxSupport;

}
