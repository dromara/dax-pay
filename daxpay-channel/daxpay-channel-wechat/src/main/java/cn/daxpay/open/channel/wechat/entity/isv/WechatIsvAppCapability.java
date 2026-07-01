package cn.daxpay.open.channel.wechat.entity.isv;

import cn.daxpay.open.channel.wechat.convert.isv.WechatIsvAppCapabilityConvert;
import cn.daxpay.open.channel.wechat.result.isv.WechatIsvAppCapabilityResult;
import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 微信服务商应用支付能力关联
///
/// 建立全局维度下「支付能力 → 服务商应用」的绑定关系，供支付时选择正确的服务商应用(微信AppId)。
/// 微信服务商应用全局共享(密钥全局唯一),故本表不挂通道商户号,同一支付能力全局只能绑定一个应用。
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "wechat_isv_app_capability", autoResultMap = true)
public class WechatIsvAppCapability extends MpBaseEntity implements ToResult<WechatIsvAppCapabilityResult> {

    /// 支付能力编码
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.PayCapabilityEnum
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String capability;

    /// 关联微信服务商应用ID(指向 wechat_isv_app.id)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Long wechatIsvAppId;

    /// 转换
    @Override
    public WechatIsvAppCapabilityResult toResult() {
        return WechatIsvAppCapabilityConvert.CONVERT.toResult(this);
    }
}
