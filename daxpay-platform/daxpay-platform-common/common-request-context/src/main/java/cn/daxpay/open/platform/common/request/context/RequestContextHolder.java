package cn.daxpay.open.platform.common.request.context;

import cn.daxpay.open.platform.common.request.context.constant.RequestContextCode;
import cn.daxpay.open.platform.common.request.context.local.RequestContextStorage;
import cn.daxpay.open.platform.core.code.WebHeaderCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

/// # 请求上下文访问工具类
///
@UtilityClass
public class RequestContextHolder {

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
