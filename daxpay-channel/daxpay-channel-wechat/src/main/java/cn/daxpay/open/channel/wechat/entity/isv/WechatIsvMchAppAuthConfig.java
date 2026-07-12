package cn.daxpay.open.channel.wechat.entity.isv;

import cn.daxpay.open.channel.wechat.convert.isv.WechatIsvMchAppAuthConfigConvert;
import cn.daxpay.open.channel.wechat.result.isv.WechatIsvMchAppAuthConfigResult;
import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 微信服务商通道商户应用授权认证配置
///
/// 配置服务商通道商户应用(子商户应用)的应用密钥,用于微信OAuth授权流程中的身份验证。
/// 一个应用对应一份授权配置(由唯一约束 channel_mch_no + wechat_isv_mch_app_id 保证)。
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "wechat_isv_mch_app_auth_config", autoResultMap = true)
public class WechatIsvMchAppAuthConfig extends MchBaseEntity implements ToResult<WechatIsvMchAppAuthConfigResult> {

    /// 通道商户号(服务商特约商户)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channelMchNo;

    /// 关联服务商通道商户应用ID
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Long wechatIsvMchAppId;

    /// 应用密钥(加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String appSecret;

    /// 转换
    @Override
    public WechatIsvMchAppAuthConfigResult toResult() {
        return WechatIsvMchAppAuthConfigConvert.CONVERT.toResult(this);
    }
}
