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

    /// 商户名称(由 mchNo 翻译, 走系统 @Trans 机制)
    @Trans(entity = MerchantInfo.class, source = MchBaseResult.Fields.mchNo, result = MerchantInfo.Fields.mchName)
    @Schema(description = "商户名称")
    private String mchName;

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

    /// 是否沙箱环境商户
    @Schema(description = "是否沙箱环境商户")
    private boolean sandbox;

    /// 当前生效环境(来自商户 sandbox 字段, 商户只读): prod/sandbox
    @Schema(description = "当前生效环境: prod/sandbox")
    private String activeEnv;

    /// 是否支持沙箱环境(决定前端是否展示环境标签)
    @Schema(description = "是否支持沙箱环境")
    private Boolean sandboxSupport;

}
