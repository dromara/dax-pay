package cn.daxpay.open.payment.douyin.entity.merchant;

import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.payment.douyin.convert.merchant.DyMchAppConvert;
import cn.daxpay.open.payment.douyin.result.merchant.DyMchAppResult;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 商户抖音应用
///
/// 商户域开放平台身份主数据：跨商户不共享，同商户跨通道可引用。
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "dy_mch_app", autoResultMap = true)
public class DyMchApp extends MchBaseEntity implements ToResult<DyMchAppResult> {

    /// 应用名称
    private String appName;

    /// 应用类型
    /// @see cn.daxpay.open.payment.douyin.enums.DyAppTypeEnum
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String appType;

    /// 抖音应用AppId
    private String douyinAppId;

    /// 转换
    @Override
    public DyMchAppResult toResult() {
        return DyMchAppConvert.CONVERT.toResult(this);
    }
}
