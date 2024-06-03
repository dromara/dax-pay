package cn.bootx.platform.common.spring.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

/**
 * web服务器工具类
 *
 * @author xxm
 * @since 2021/8/13
 */
@UtilityClass
public class WebServletUtil {

    /**
     * 获取http请求
     */
    public HttpServletRequest getRequest() {
        return Optional.ofNullable(getRequestAttributes()).map(ServletRequestAttributes::getRequest).orElse(null);
    }

    /**
     * 获取请求类型
     */
    public String getMethod() {
        return Optional.ofNullable(getRequest()).map(HttpServletRequest::getMethod).orElse(null);
    }

    /**
     * 获取请求路径
     */
    public String getPath() {
        return Optional.ofNullable(getRequest()).map(HttpServletRequest::getRequestURI).orElse(null);
    }

    /**
     * 获取参数
     */
    public String getParameter(String name) {
        return Optional.ofNullable(getRequest()).map(o -> o.getParameter(name)).orElse(null);
    }

    /**
     * 获取http响应
     */
    public HttpServletResponse getResponse() {
        return Optional.ofNullable(getRequestAttributes()).map(ServletRequestAttributes::getResponse).orElse(null);
    }

    /**
     * 获取RequestAttributes
     */
    public ServletRequestAttributes getRequestAttributes() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        return (ServletRequestAttributes) attributes;
    }

}
