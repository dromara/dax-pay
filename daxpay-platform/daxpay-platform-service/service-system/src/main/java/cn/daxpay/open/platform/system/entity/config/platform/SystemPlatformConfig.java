package cn.daxpay.open.platform.system.entity.config.platform;

import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.handler.type.JsonbStringTypeHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 系统平台统一配置
///
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "system_platform_config",autoResultMap = true)
public class SystemPlatformConfig extends MpBaseEntity {

    /// 配置类型
    private String configType;

    /// 配置名称
    private String configName;

    /// 配置数据JSON格式
    @TableField(typeHandler = JsonbStringTypeHandler.class)
    private String configData;

    /// 配置描述
    private String description;

    /// 是否启用
    private boolean enabled;

}
