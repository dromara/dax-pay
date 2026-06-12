package org.dromara.daxpay.platform.system.entity.config.sms;

import org.dromara.daxpay.platform.common.mybatisplus.base.MpBaseEntity;
import org.dromara.daxpay.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import org.dromara.daxpay.platform.core.enums.common.SmsProviderEnum;
import org.dromara.daxpay.platform.system.convert.PlatformConfigConvert;
import org.dromara.daxpay.platform.system.result.config.platform.PlatformSmsConfigResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 平台短信配置
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "system_platform_sms_config", autoResultMap = true)
public class PlatformSmsConfig extends MpBaseEntity implements ToResult<PlatformSmsConfigResult> {

    /// 配置名称
    private String configName;

    /// 是否为默认
    private boolean enable;

    /// 短信供应商
    /// @see SmsProviderEnum
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String provider;

    /// 短信模板ID
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String templateId;

    /// 短信签名
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String signature;

    /// AccessKey
    @TableField(updateStrategy = FieldStrategy.ALWAYS, typeHandler = DataEncryptTypeHandler.class)
    private String accessKey;

    /// SecretKey
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String secretKey;

    /// 商户注册模板
    private String registerId;

    /// 忘记密码模板
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String forgetId;

    /// 验证码模板
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String captchaId;

    /// 通知模板
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String noticeId;

    /// 转换为结果对象
    @Override
    public PlatformSmsConfigResult toResult() {
        return PlatformConfigConvert.CONVERT.toResult(this);
    }
}

