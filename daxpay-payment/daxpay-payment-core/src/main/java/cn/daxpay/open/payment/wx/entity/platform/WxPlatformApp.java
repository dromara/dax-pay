package cn.daxpay.open.payment.wx.entity.platform;

import cn.daxpay.open.payment.wx.convert.platform.WxPlatformAppConvert;
import cn.daxpay.open.payment.wx.result.platform.WxPlatformAppResult;
import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 平台微信应用
///
/// 开放平台身份主数据（跨通道可引用），无商户行级隔离。
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "wx_platform_app", autoResultMap = true)
public class WxPlatformApp extends MpBaseEntity implements ToResult<WxPlatformAppResult> {

    /// 应用名称
    private String appName;

    /// 应用类型
    /// @see cn.daxpay.open.payment.wx.enums.WxAppTypeEnum
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String appType;

    /// 微信应用AppId
    private String wxAppId;

    /// 应用密钥(加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String appSecret;

    /// 转换
    @Override
    public WxPlatformAppResult toResult() {
        return WxPlatformAppConvert.CONVERT.toResult(this);
    }
}
