package cn.daxpay.open.platform.system.entity.mobile;

import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import cn.daxpay.open.platform.system.convert.mobile.MobileAppConvert;
import cn.daxpay.open.platform.system.result.mobile.MobileAppResult;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 移动端应用配置
///
/// 平台级移动端应用配置, 按端类型(appType)+移动平台(platform)维度, 每组合一条记录。
/// app_config/notify_config 以 JSON 文本存储, 通过 [DataEncryptTypeHandler] AES-256-GCM 加密入库。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "pay_platform_mobile_app", autoResultMap = true)
public class MobileApp extends MpBaseEntity implements ToResult<MobileAppResult> {

    /// 端类型
    /// @see cn.daxpay.open.platform.system.enums.MobileAppTypeEnum
    private String appType;

    /// 移动平台
    /// @see cn.daxpay.open.platform.system.enums.MobilePlatformEnum
    private String platform;

    /// 应用名称(展示用)
    private String appName;

    /// 平台特有密钥配置(JSON文本, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String appConfig;

    /// 消息通知配置(JSON文本, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
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
