package org.dromara.daxpay.platform.iam.entity.config;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/// # 配置变更历史实体
///
/// 记录安全配置的所有变更历史
@Data
@Accessors(chain = true)
public class ConfigChangeHistory {

    /// 主键 ID
    private Long id;

    /// 配置分组
    private String configGroup;

    /// 配置项键名
    private String configKey;

    /// 旧值
    private String oldValue;

    /// 新值
    private String newValue;

    /// 修改人 ID
    private Long modifiedBy;

    /// 修改人用户名
    private String modifiedByUsername;

    /// 修改时间
    private OffsetDateTime modifiedAt;

    /// 修改原因/备注
    private String remark;

    protected void onCreate() {
        if (modifiedAt == null) {
            modifiedAt = OffsetDateTime.now(ZoneOffset.UTC);
        }
    }
}
