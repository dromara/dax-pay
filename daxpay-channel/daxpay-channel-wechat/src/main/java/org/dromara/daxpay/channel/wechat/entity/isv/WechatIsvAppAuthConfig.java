package org.dromara.daxpay.channel.wechat.entity.isv;

import org.dromara.daxpay.channel.wechat.convert.isv.WechatIsvAppAuthConfigConvert;
import org.dromara.daxpay.channel.wechat.result.isv.WechatIsvAppAuthConfigResult;
import org.dromara.daxpay.platform.common.mybatisplus.base.MpBaseEntity;
import org.dromara.daxpay.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 微信服务商应用授权认证配置
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "wechat_isv_app_auth_config", autoResultMap = true)
public class WechatIsvAppAuthConfig extends MpBaseEntity implements ToResult<WechatIsvAppAuthConfigResult> {

    /// 微信服务商应用ID
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Long appId;

    /// 应用密钥(加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String appSecret;

    /// 授权回调地址
    private String authCallbackUrl;

    /// 转换
    @Override
    public WechatIsvAppAuthConfigResult toResult() {
        return WechatIsvAppAuthConfigConvert.CONVERT.toResult(this);
    }
}
