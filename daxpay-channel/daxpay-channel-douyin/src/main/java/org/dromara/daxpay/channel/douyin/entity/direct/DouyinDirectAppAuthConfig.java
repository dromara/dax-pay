package org.dromara.daxpay.channel.douyin.entity.direct;

import org.dromara.daxpay.channel.douyin.convert.direct.DouyinDirectAppAuthConfigConvert;
import org.dromara.daxpay.channel.douyin.result.direct.DouyinDirectAppAuthConfigResult;
import org.dromara.daxpay.payment.common.entity.merchant.MchBaseEntity;
import org.dromara.daxpay.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 抖音直连商户应用授权认证配置
///
/// 配置直连商户应用的授权回调地址，用于抖音用户授权流程中的回调跳转。
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "douyin_direct_app_auth_config", autoResultMap = true)
public class DouyinDirectAppAuthConfig extends MchBaseEntity implements ToResult<DouyinDirectAppAuthConfigResult> {

    /// 通道商户号
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channelMchNo;

    /// 关联应用ID
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Long douyinDirectAppId;

    /// 授权回调地址
    private String authCallbackUrl;

    /// 转换
    @Override
    public DouyinDirectAppAuthConfigResult toResult() {
        return DouyinDirectAppAuthConfigConvert.CONVERT.toResult(this);
    }
}
