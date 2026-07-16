package cn.daxpay.open.platform.capability.cache.secure;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/// # 敏感缓存名匹配器
///
/// 判断 cacheName 是否为敏感缓存（S2）：
/// - 名称以 [securePrefix] 开头（默认 `secure:`）
/// - 或落在 [secureNames] 精确列表中
///
/// 匹配的缓存 L2 value 使用整包 AES-GCM 加密；未启用数据加密时则仅 L1、不写 Redis。
public class SecureCacheNameMatcher {

    private final String securePrefix;
    private final Set<String> secureNames;

    public SecureCacheNameMatcher(String securePrefix, List<String> secureNames) {
        this.securePrefix = StrUtil.blankToDefault(securePrefix, "secure:");
        if (CollUtil.isEmpty(secureNames)) {
            this.secureNames = Collections.emptySet();
        } else {
            this.secureNames = new HashSet<>(secureNames);
        }
    }

    /// 是否为敏感缓存名
    public boolean matches(String cacheName) {
        if (StrUtil.isBlank(cacheName)) {
            return false;
        }
        if (cacheName.startsWith(this.securePrefix)) {
            return true;
        }
        return this.secureNames.contains(cacheName);
    }

    public String getSecurePrefix() {
        return this.securePrefix;
    }
}
