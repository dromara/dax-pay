package cn.daxpay.open.payment.douyin.entity.platform;

import cn.daxpay.open.payment.douyin.convert.platform.DyPlatformAppCapabilityConvert;
import cn.daxpay.open.payment.douyin.result.platform.DyPlatformAppCapabilityResult;
import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 平台抖音应用默认能力绑定
///
/// 按支付产品隔离：「支付能力 → 平台抖音应用」；同产品下同一能力仅一条。
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "dy_platform_app_capability", autoResultMap = true)
public class DyPlatformAppCapability extends MpBaseEntity implements ToResult<DyPlatformAppCapabilityResult> {

    /// 支付产品编码
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String product;

    /// 支付能力编码
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.PayCapabilityEnum
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String capability;

    /// 平台抖音应用ID
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Long dyPlatformAppId;

    /// 转换
    @Override
    public DyPlatformAppCapabilityResult toResult() {
        return DyPlatformAppCapabilityConvert.CONVERT.toResult(this);
    }
}
