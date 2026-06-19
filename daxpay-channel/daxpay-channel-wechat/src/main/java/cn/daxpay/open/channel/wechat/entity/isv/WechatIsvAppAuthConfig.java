package cn.daxpay.open.channel.wechat.entity.isv;

import cn.daxpay.open.channel.wechat.convert.isv.WechatIsvAppAuthConfigConvert;
import cn.daxpay.open.channel.wechat.result.isv.WechatIsvAppAuthConfigResult;
import cn.daxpay.open.payment.common.entity.merchant.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 微信服务商应用授权认证配置
///
/// 继承商户基础实体，自动填充商户号用于多商户隔离。
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "wechat_isv_app_auth_config", autoResultMap = true)
public class WechatIsvAppAuthConfig extends MchBaseEntity implements ToResult<WechatIsvAppAuthConfigResult> {

    /// 通道商户号
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channelMchNo;

    /// 微信服务商应用ID
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Long wechatIsvAppId;

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
