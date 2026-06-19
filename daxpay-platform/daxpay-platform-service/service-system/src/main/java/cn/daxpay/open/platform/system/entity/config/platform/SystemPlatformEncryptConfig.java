package cn.daxpay.open.platform.system.entity.config.platform;

import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 系统平台加密配置
///
/// 用于存储敏感配置数据，使用 AES-256-GCM 加密
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "system_platform_encrypt_config", autoResultMap = true)
public class SystemPlatformEncryptConfig extends MpBaseEntity {

    /// 配置类型
    private String configType;

    /// 配置名称
    private String configName;

    /// 配置数据(加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String configData;

    /// 配置描述
    private String description;

    /// 是否启用
    private boolean enabled;
}
