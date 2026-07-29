package cn.daxpay.open.payment.wx.entity.channel;

import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.payment.wx.convert.channel.WxChannelAppCapabilityConvert;
import cn.daxpay.open.payment.wx.result.channel.WxChannelAppCapabilityResult;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 通道商户微信应用能力绑定
///
/// 通道商户 × 支付能力 × 档位 绑定主数据引用；同能力可按档位双绑 platform + merchant。
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "wx_channel_app_capability", autoResultMap = true)
public class WxChannelAppCapability extends MchBaseEntity implements ToResult<WxChannelAppCapabilityResult> {

    /// 通道商户号
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channelMchNo;

    /// 支付能力编码
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.PayCapabilityEnum
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String capability;

    /// 应用档位
    /// @see cn.daxpay.open.payment.auth.core.AppScopeEnum
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String appScope;

    /// 微信应用主数据主键（由 appScope 决定指向平台或商户表；可换绑）
    private Long wxAppRefId;

    /// 转换
    @Override
    public WxChannelAppCapabilityResult toResult() {
        return WxChannelAppCapabilityConvert.CONVERT.toResult(this);
    }
}
