package cn.daxpay.open.payment.app.mobile.entity;

import cn.daxpay.open.payment.app.mobile.convert.MobileAppConvert;
import cn.daxpay.open.payment.app.mobile.result.MobileAppResult;
import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.common.mybatisplus.handler.type.JsonbStringTypeHandler;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 移动端应用配置
///
/// 平台级移动端应用配置, 按端类型(appType)+移动平台(platform)维度, 每组合一条记录。
/// app_config 存储各平台特有的密钥配置(jsonb), notify_config 存储消息通知配置(jsonb)。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "pay_platform_mobile_app", autoResultMap = true)
public class MobileApp extends MpBaseEntity implements ToResult<MobileAppResult> {

    /// 端类型
    /// @see cn.daxpay.open.payment.app.mobile.enums.MobileAppTypeEnum
    private String appType;

    /// 移动平台
    /// @see cn.daxpay.open.payment.app.mobile.enums.MobilePlatformEnum
    private String platform;

    /// 应用名称(展示用)
    private String appName;

    /// 平台特有密钥配置(jsonb原始JSON文本)
    @com.baomidou.mybatisplus.annotation.TableField(typeHandler = JsonbStringTypeHandler.class)
    private String appConfig;

    /// 消息通知配置(jsonb原始JSON文本)
    @com.baomidou.mybatisplus.annotation.TableField(typeHandler = JsonbStringTypeHandler.class)
    private String notifyConfig;

    /// 是否启用第三方账号用户绑定
    private Boolean bindingEnabled;

    /// 是否启用
    private Boolean enabled;

    /// 备注
    private String remark;

    @Override
    public MobileAppResult toResult() {
        return MobileAppConvert.CONVERT.toResult(this);
    }
}
