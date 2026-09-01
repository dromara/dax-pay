package cn.daxpay.open.platform.capability.auth.interceptor;

import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.code.WebHeaderCode;
import cn.daxpay.open.platform.core.enums.client.ClientEnum;
import cn.daxpay.open.platform.capability.auth.exception.NotLoginException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

/// # 客户端身份域守卫拦截器
///
/// 校验请求头 `x-client-code` 与端点所属身份域一致, 不一致时抛 [NotLoginException] 返回 401,
/// 前端统一按"登录态失效"处理(清 token + 跳转登录页)。
///
/// ## 背景
/// Sa-Token 鉴权只校验登录态、不校验身份域, 其他端(如运营端)的有效凭证访问商户端专属端点
/// (`/app-mch/**`、`/mch/**`)时会被放行, 随后商户上下文(`MchContextLocalFilter`)因身份域不符
/// 不装载商户号, 业务层抛出"数据错误, 未发现商户号"(HTTP 200 业务错), 前端无法感知登录态问题,
/// 既不踢下线也不跳登录页。
///
/// ## 用法
/// 由各业务模块在 WebMvcConfigurer 中注册并声明端点前缀:
/// ```
/// registry.addInterceptor(new ClientCodeGuardInterceptor(ClientEnum.MERCHANT))
///         .addPathPatterns("/app-mch/**")
///         .order(1);  // 晚于 Sa-Token 鉴权(未登录优先报"用户未登录")
/// ```
public class ClientCodeGuardInterceptor implements HandlerInterceptor {

    private final ClientEnum expectClient;

    public ClientCodeGuardInterceptor(ClientEnum expectClient) {
        this.expectClient = expectClient;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String clientCode = request.getHeader(WebHeaderCode.X_CLIENT_CODE);
        if (!expectClient.getCode().equals(clientCode)) {
            // 返回 401 触发前端"登录态失效"处理(清 token + 跳登录页)
            throw new NotLoginException(CommonErrorCode.AUTHENTICATION_FAIL, "error.auth.clientCodeMismatch");
        }
        return true;
    }
}
