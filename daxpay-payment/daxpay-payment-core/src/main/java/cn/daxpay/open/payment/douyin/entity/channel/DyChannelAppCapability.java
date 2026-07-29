package cn.daxpay.open.payment.douyin.entity.channel;

import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.payment.douyin.convert.channel.DyChannelAppCapabilityConvert;
import cn.daxpay.open.payment.douyin.result.channel.DyChannelAppCapabilityResult;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 通道商户抖音应用能力绑定
///
/// 通道商户 × 支付能力 × 档位 绑定主数据引用；同能力可按档位双绑 platform + merchant。
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "dy_channel_app_capability", autoResultMap = true)
public class DyChannelAppCapability extends MchBaseEntity implements ToResult<DyChannelAppCapabilityResult> {

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

    /// 抖音应用主数据主键（由 appScope 决定指向平台或商户表；可换绑）
    private Long dyAppRefId;

    /// 转换
    @Override
    public DyChannelAppCapabilityResult toResult() {
        return DyChannelAppCapabilityConvert.CONVERT.toResult(this);
    }
}
