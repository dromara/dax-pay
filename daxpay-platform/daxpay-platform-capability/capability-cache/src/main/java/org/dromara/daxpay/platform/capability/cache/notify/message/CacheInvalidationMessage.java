package org.dromara.daxpay.platform.capability.cache.notify.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/// # 缓存失效消息
///
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CacheInvalidationMessage {

    /// 缓存名称
    private String cacheName;

    /// 缓存键，clear 时可为空
    private String key;

    /// 失效类型
    private String type;
}
