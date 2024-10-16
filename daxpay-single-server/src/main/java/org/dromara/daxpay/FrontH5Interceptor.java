package org.dromara.daxpay;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 移动端前端转发
 * @author xxm
 * @since 2024/10/6
 */
@Component
public class FrontH5Interceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();

        // 检查请求 URI 是否包含 "."（即文件后缀名）
        if (requestURI.contains(".")) {
            // 对于静态资源，继续处理
            return true;
        }
        // 不包含后缀的路径，转发到 index.html
        request.getRequestDispatcher("/h5/index.html").forward(request, response);
        // 阻止继续处理请求
        return false;
    }
}
