package cn.daxpay.open.channel.wechat.entity.isv;

import cn.daxpay.open.channel.wechat.convert.isv.WechatIsvMchAppCapabilityConvert;
import cn.daxpay.open.channel.wechat.result.isv.WechatIsvMchAppCapabilityResult;
import cn.daxpay.open.payment.common.entity.merchant.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 微信服务商通道商户应用支付能力关联
///
/// 建立通道商户维度下「支付能力 → 子商户应用」的绑定关系,供支付时选择正确的微信应用(sub_appid)。
/// 本表只存子商户显式选择自己应用的记录,某能力未配置时自动回退到全局服务商应用配置。
/// 同一通道商户下,一个支付能力只能绑定一个应用(由唯一约束 channel_mch_no + capability 保证)。
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "wechat_isv_mch_app_capability", autoResultMap = true)
public class WechatIsvMchAppCapability extends MchBaseEntity implements ToResult<WechatIsvMchAppCapabilityResult> {

    /// 通道商户号(服务商特约商户)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channelMchNo;

    /// 支付能力编码
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.PayCapabilityEnum
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String capability;

    /// 关联微信服务商通道商户应用ID(指向 wechat_isv_mch_app.id)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Long wechatIsvMchAppId;

    /// 转换
    @Override
    public WechatIsvMchAppCapabilityResult toResult() {
        return WechatIsvMchAppCapabilityConvert.CONVERT.toResult(this);
    }
}
