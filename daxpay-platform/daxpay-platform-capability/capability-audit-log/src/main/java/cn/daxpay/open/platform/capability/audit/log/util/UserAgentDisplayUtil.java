package cn.daxpay.open.platform.capability.audit.log.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.useragent.UserAgent;
import lombok.experimental.UtilityClass;

/// # 用户代理展示名
///
/// Hutool 对无法识别的 UA 返回浏览器名 "Unknown", 版本号为空时字符串拼接会得到 "Unknown null";
/// 日志落库前统一归一, 避免列表与详情展示无意义片段。
@UtilityClass
public class UserAgentDisplayUtil {

    /// 浏览器展示名(浏览器名 + 版本), 无法识别时返回 null
    /// @param userAgent 解析后的 UA 信息
    /// @return 浏览器展示名
    public String browser(UserAgent userAgent) {
        if (userAgent == null) {
            return null;
        }
        String name = userAgent.getBrowser().getName();
        if (StrUtil.isBlank(name) || StrUtil.equalsIgnoreCase("Unknown", name)) {
            return null;
        }
        String version = userAgent.getVersion();
        return StrUtil.isBlank(version) ? name : name + " " + version;
    }

    /// 操作系统展示名, 无法识别时返回 null
    /// @param userAgent 解析后的 UA 信息
    /// @return 操作系统展示名
    public String os(UserAgent userAgent) {
        if (userAgent == null) {
            return null;
        }
        String name = userAgent.getOs().getName();
        return StrUtil.isBlank(name) || StrUtil.equalsIgnoreCase("Unknown", name) ? null : name;
    }
}
