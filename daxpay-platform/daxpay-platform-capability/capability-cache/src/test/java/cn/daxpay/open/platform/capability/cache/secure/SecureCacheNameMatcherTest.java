package cn.daxpay.open.platform.capability.cache.secure;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/// # 敏感缓存名匹配器测试
class SecureCacheNameMatcherTest {

    @Test
    @DisplayName("前缀匹配 secure:")
    void shouldMatchSecurePrefix() {
        SecureCacheNameMatcher matcher = new SecureCacheNameMatcher("secure:", List.of());
        assertTrue(matcher.matches("secure:channel-key:wechat-isv"));
        assertTrue(matcher.matches("secure:merchant-credential"));
        assertFalse(matcher.matches("system:dict"));
        assertFalse(matcher.matches("payment:channel-merchant"));
    }

    @Test
    @DisplayName("精确列表命中")
    void shouldMatchExactNames() {
        SecureCacheNameMatcher matcher = new SecureCacheNameMatcher("secure:", List.of("legacy:secret-config"));
        assertTrue(matcher.matches("legacy:secret-config"));
        assertFalse(matcher.matches("legacy:other"));
    }

    @Test
    @DisplayName("空名称不匹配")
    void shouldNotMatchBlank() {
        SecureCacheNameMatcher matcher = new SecureCacheNameMatcher("secure:", null);
        assertFalse(matcher.matches(null));
        assertFalse(matcher.matches(""));
        assertFalse(matcher.matches("   "));
    }

    @Test
    @DisplayName("默认前缀")
    void shouldDefaultPrefixWhenBlank() {
        SecureCacheNameMatcher matcher = new SecureCacheNameMatcher(null, List.of());
        assertEquals("secure:", matcher.getSecurePrefix());
        assertTrue(matcher.matches("secure:demo"));
    }
}
