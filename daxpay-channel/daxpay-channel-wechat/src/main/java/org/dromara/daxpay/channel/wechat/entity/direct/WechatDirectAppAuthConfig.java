package org.dromara.daxpay.channel.wechat.entity.direct;

import org.dromara.daxpay.channel.wechat.convert.direct.WechatDirectAppAuthConfigConvert;
import org.dromara.daxpay.channel.wechat.result.direct.WechatDirectAppAuthConfigResult;
import org.dromara.daxpay.payment.common.entity.merchant.MchBaseEntity;
import org.dromara.daxpay.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 微信直连商户应用授权认证配置
///
/// 配置直连商户应用的应用密钥和授权回调地址，用于微信OAuth授权流程中的身份验证与回调跳转。
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "wechat_direct_app_auth_config", autoResultMap = true)
public class WechatDirectAppAuthConfig extends MchBaseEntity implements ToResult<WechatDirectAppAuthConfigResult> {

    /// 通道商户号
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channelMchNo;

    /// 关联应用ID
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Long wechatDirectAppId;

    /// 应用密钥(加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String appSecret;

    /// 授权回调地址
    private String authCallbackUrl;

    /// 转换
    @Override
    public WechatDirectAppAuthConfigResult toResult() {
        return WechatDirectAppAuthConfigConvert.CONVERT.toResult(this);
    }
}
