package cn.daxpay.open.channel.douyin.entity.direct;

import cn.daxpay.open.channel.douyin.convert.direct.DouyinDirectAppAuthConfigConvert;
import cn.daxpay.open.channel.douyin.result.direct.DouyinDirectAppAuthConfigResult;
import cn.daxpay.open.payment.common.entity.merchant.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 抖音直连商户应用授权认证配置
///
/// 配置直连商户应用的应用密钥和授权回调地址，用于抖音用户授权流程中的身份验证与回调跳转。
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

    /// 应用密钥(加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String appSecret;

    /// 授权回调地址
    private String authCallbackUrl;

    /// 转换
    @Override
    public DouyinDirectAppAuthConfigResult toResult() {
        return DouyinDirectAppAuthConfigConvert.CONVERT.toResult(this);
    }
}
