package cn.daxpay.open.channel.alipay.entity.direct;

import cn.daxpay.open.channel.alipay.convert.direct.AlipayDirectAppCapabilityConvert;
import cn.daxpay.open.channel.alipay.result.direct.AlipayDirectAppCapabilityResult;
import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 支付宝直连商户应用支付能力关联
///
/// 建立通道商户维度下「支付能力 → 应用」的绑定关系，供支付时选择正确的支付宝应用(APPID)。
/// 同一通道商户下，一个支付能力只能绑定一个应用(由唯一约束 channel_mch_no + capability 保证)。
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "alipay_direct_app_capability", autoResultMap = true)
public class AlipayDirectAppCapability extends MchBaseEntity implements ToResult<AlipayDirectAppCapabilityResult> {

    /// 通道商户号
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channelMchNo;

    /// 支付能力编码
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.PayCapabilityEnum
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String capability;

    /// 关联支付宝直连应用ID(指向 alipay_direct_app.id)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Long alipayDirectAppId;

    /// 转换
    @Override
    public AlipayDirectAppCapabilityResult toResult() {
        return AlipayDirectAppCapabilityConvert.CONVERT.toResult(this);
    }
}
