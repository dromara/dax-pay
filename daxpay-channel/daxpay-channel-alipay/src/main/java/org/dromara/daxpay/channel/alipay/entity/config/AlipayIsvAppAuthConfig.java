package org.dromara.daxpay.channel.alipay.entity.config;

import org.dromara.daxpay.channel.alipay.convert.AlipayIsvAppAuthConfigConvert;
import org.dromara.daxpay.channel.alipay.result.config.AlipayIsvAppAuthConfigResult;
import org.dromara.daxpay.platform.common.mybatisplus.base.MpBaseEntity;
import org.dromara.daxpay.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 支付宝服务商应用授权认证配置
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "alipay_isv_app_auth_config", autoResultMap = true)
public class AlipayIsvAppAuthConfig extends MpBaseEntity implements ToResult<AlipayIsvAppAuthConfigResult> {

    /// 支付宝服务商应用ID
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Long appId;

    /// 用户标识类型
    private String userIdType;

    /// 授权回调地址
    private String authCallbackUrl;

    /// 转换
    @Override
    public AlipayIsvAppAuthConfigResult toResult() {
        return AlipayIsvAppAuthConfigConvert.CONVERT.toResult(this);
    }
}
