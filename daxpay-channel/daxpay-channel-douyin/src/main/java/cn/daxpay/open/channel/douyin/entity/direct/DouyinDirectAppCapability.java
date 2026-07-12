package cn.daxpay.open.channel.douyin.entity.direct;

import cn.daxpay.open.channel.douyin.convert.direct.DouyinDirectAppCapabilityConvert;
import cn.daxpay.open.channel.douyin.result.direct.DouyinDirectAppCapabilityResult;
import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 抖音直连商户应用支付能力关联
///
/// 建立通道商户维度下「支付能力 → 直连应用」的绑定关系，供支付时选择正确的抖音应用(AppId)。
/// 同一通道商户下，一个支付能力只能绑定一个应用(由唯一约束 channel_mch_no + capability 保证)。
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "douyin_direct_app_capability", autoResultMap = true)
public class DouyinDirectAppCapability extends MchBaseEntity implements ToResult<DouyinDirectAppCapabilityResult> {

    /// 通道商户号
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channelMchNo;

    /// 支付能力编码
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.PayCapabilityEnum
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String capability;

    /// 关联抖音直连应用ID(指向 douyin_direct_app.id)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Long douyinDirectAppId;

    /// 转换
    @Override
    public DouyinDirectAppCapabilityResult toResult() {
        return DouyinDirectAppCapabilityConvert.CONVERT.toResult(this);
    }
}
