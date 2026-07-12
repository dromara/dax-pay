package cn.daxpay.open.channel.wechat.entity.direct;

import cn.daxpay.open.channel.wechat.convert.direct.WechatDirectAppCapabilityConvert;
import cn.daxpay.open.channel.wechat.result.direct.WechatDirectAppCapabilityResult;
import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 微信直连商户应用支付能力关联
///
/// 建立通道商户维度下「支付能力 → 直连应用」的绑定关系，供支付时选择正确的微信应用(AppId)。
/// 同一通道商户下，一个支付能力只能绑定一个应用(由唯一约束 channel_mch_no + capability 保证)。
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "wechat_direct_app_capability", autoResultMap = true)
public class WechatDirectAppCapability extends MchBaseEntity implements ToResult<WechatDirectAppCapabilityResult> {

    /// 通道商户号
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channelMchNo;

    /// 支付能力编码
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.PayCapabilityEnum
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String capability;

    /// 关联微信直连应用ID(指向 wechat_direct_app.id)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Long wechatDirectAppId;

    /// 转换
    @Override
    public WechatDirectAppCapabilityResult toResult() {
        return WechatDirectAppCapabilityConvert.CONVERT.toResult(this);
    }
}
