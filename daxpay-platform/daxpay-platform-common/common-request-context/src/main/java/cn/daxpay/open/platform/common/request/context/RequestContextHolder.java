package cn.daxpay.open.platform.common.request.context;

import cn.daxpay.open.platform.common.request.context.constant.RequestContextCode;
import cn.daxpay.open.platform.common.request.context.local.RequestContextStorage;
import cn.daxpay.open.platform.core.code.WebHeaderCode;
import cn.daxpay.open.platform.core.util.DateTimeUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.DateTimeException;
import java.time.ZoneId;
import java.util.Objects;

/// # 请求上下文访问工具类
///
@UtilityClass
public class RequestContextHolder {

    /// 时区标识最大长度(超长视为非法, 直接兜底)
    private static final int MAX_TIME_ZONE_LENGTH = 64;

    /// 获取请求头参数
    public String getHeader(String name) {
        String header = RequestContextStorage.get(name);
        if (Objects.nonNull(header)) {
            return header;
        }
        return getWebHeader(name);
    }

    /// 获取追踪ID
    public String getTraceId() {
        return getHeader(WebHeaderCode.X_TRACE_ID);
    }

    /// 获取身份域编码
    public String getClientCode() {
        return getHeader(WebHeaderCode.X_CLIENT_CODE);
    }

    /// 获取请求终端(壳维度: web/app), 与 clientCode 正交, 未携带请求头时返回 null 由调用方兜底
    public String getTerminal() {
        return getHeader(WebHeaderCode.X_TERMINAL);
    }

    /// 获取国际化语言
    public String getLanguage() {
        return getHeader(WebHeaderCode.ACCEPT_LANGUAGE);
    }

    /// 获取用户时区, 用于终端展示类格式化(如 Excel 导出时间列)
    ///
    /// 取请求头 `x-timezone`(IANA 标识, 如 Asia/Chongqing), 缺失或非法时兜底业务时区 [DateTimeUtil#ZONE_CST],
    /// 不抛异常以免影响主流程。注意与**时间传输**语义的区别: 接口出入参始终为 UTC/带偏移的 ISO 串,
    /// 本时区只影响没有前端转换环节的终点展示(导出文件、对外报文等)。
    public ZoneId getTimeZone() {
        String timeZone = getHeader(WebHeaderCode.X_TIME_ZONE);
        if (StrUtil.isBlank(timeZone) || timeZone.length() > MAX_TIME_ZONE_LENGTH) {
            return DateTimeUtil.ZONE_CST;
        }
        try {
            return ZoneId.of(timeZone);
        }
        // 非法时区标识(伪造/拼写错误)统一兜底, 不阻断业务
        catch (DateTimeException e) {
            return DateTimeUtil.ZONE_CST;
        }
    }

    /// 获取请求方法
    public String getMethod() {
        return RequestContextStorage.get(RequestContextCode.METHOD);
    }

    /// 获取上下文路径
    public String getContextPath() {
        return RequestContextStorage.get(RequestContextCode.CONTEXT_PATH);
    }

    /// 获取请求URI
    public String getRequestUri() {
        return RequestContextStorage.get(RequestContextCode.REQUEST_URI);
    }

    /// 获取请求URL
    public String getRequestUrl() {
        return RequestContextStorage.get(RequestContextCode.REQUEST_URL);
    }

    private String getWebHeader(String name) {
        RequestAttributes requestAttributes = org.springframework.web.context.request.RequestContextHolder.getRequestAttributes();
        if (!(requestAttributes instanceof ServletRequestAttributes servletRequestAttributes)) {
            return null;
        }
        HttpServletRequest request = servletRequestAttributes.getRequest();
        return request.getHeader(name);
    }
}
