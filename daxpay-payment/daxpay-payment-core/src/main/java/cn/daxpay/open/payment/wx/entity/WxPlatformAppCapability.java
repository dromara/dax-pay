package cn.daxpay.open.payment.wx.entity;

import cn.daxpay.open.payment.wx.convert.WxPlatformAppCapabilityConvert;
import cn.daxpay.open.payment.wx.result.WxPlatformAppCapabilityResult;
import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 平台微信应用默认能力绑定
///
/// 按支付产品隔离：「支付能力 → 平台微信应用」；同产品下同一能力仅一条。
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "wx_platform_app_capability", autoResultMap = true)
public class WxPlatformAppCapability extends MpBaseEntity implements ToResult<WxPlatformAppCapabilityResult> {

    /// 支付产品编码
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String product;

    /// 支付能力编码
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.PayCapabilityEnum
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String capability;

    /// 平台微信应用ID
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Long wxPlatformAppId;

    /// 转换
    @Override
    public WxPlatformAppCapabilityResult toResult() {
        return WxPlatformAppCapabilityConvert.CONVERT.toResult(this);
    }
}
