package cn.daxpay.open.platform.capability.audit.log.util;

import cn.hutool.http.useragent.Browser;
import cn.hutool.http.useragent.OS;
import cn.hutool.http.useragent.UserAgent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/// # UserAgentDisplayUtil 单元测试
///
/// 锁定日志落库口径: 无法识别的 UA 不写 "Unknown" / "Unknown null", 版本为空时只留浏览器名
class UserAgentDisplayUtilTest {

    @Test
    @DisplayName("浏览器: Unknown + 空版本 → null")
    void browser_unknown_shouldBeNull() {
        UserAgent userAgent = new UserAgent();
        userAgent.setBrowser(Browser.Unknown);
        userAgent.setVersion(null);

        assertNull(UserAgentDisplayUtil.browser(userAgent));
    }

    @Test
    @DisplayName("浏览器: 有名无版本 → 只保留名称, 不再拼出 null")
    void browser_withoutVersion_shouldDropSuffix() {
        UserAgent userAgent = new UserAgent();
        userAgent.setBrowser(new Browser("Chrome", "Chrome", "1"));
        userAgent.setVersion(null);

        assertEquals("Chrome", UserAgentDisplayUtil.browser(userAgent));
    }

    @Test
    @DisplayName("浏览器: 名称 + 版本 → 正常拼接")
    void browser_withVersion_shouldJoin() {
        UserAgent userAgent = new UserAgent();
        userAgent.setBrowser(new Browser("Chrome", "Chrome", "1"));
        userAgent.setVersion("120.0");

        assertEquals("Chrome 120.0", UserAgentDisplayUtil.browser(userAgent));
    }

    @Test
    @DisplayName("操作系统: Unknown → null, 正常名称原样返回")
    void os_shouldNormalizeUnknown() {
        UserAgent unknown = new UserAgent();
        unknown.setOs(OS.Unknown);
        assertNull(UserAgentDisplayUtil.os(unknown));

        UserAgent windows = new UserAgent();
        windows.setOs(new OS("Windows 10", "Windows"));
        assertEquals("Windows 10", UserAgentDisplayUtil.os(windows));
    }
}
