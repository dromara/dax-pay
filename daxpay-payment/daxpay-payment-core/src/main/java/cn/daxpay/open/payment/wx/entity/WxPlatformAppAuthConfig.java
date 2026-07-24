package cn.daxpay.open.payment.wx.entity;

import cn.daxpay.open.payment.wx.convert.WxPlatformAppAuthConfigConvert;
import cn.daxpay.open.payment.wx.result.WxPlatformAppAuthConfigResult;
import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 平台微信应用授权认证配置
///
/// 平台应用本身为全局配置，其 appSecret 同样为全局配置。
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "wx_platform_app_auth_config", autoResultMap = true)
public class WxPlatformAppAuthConfig extends MpBaseEntity implements ToResult<WxPlatformAppAuthConfigResult> {

    /// 平台微信应用ID
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Long wxPlatformAppId;

    /// 应用密钥(加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String appSecret;

    /// 转换
    @Override
    public WxPlatformAppAuthConfigResult toResult() {
        return WxPlatformAppAuthConfigConvert.CONVERT.toResult(this);
    }
}
