package cn.daxpay.open.platform.capability.auth.handler;

import cn.daxpay.open.platform.common.i18n.util.I18nUtil;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.capability.auth.exception.NotLoginException;
import cn.daxpay.open.platform.capability.auth.exception.RouterCheckException;
import cn.dev33.satoken.exception.SaTokenException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

/// # 过滤SaTokenException,需要运行在 RestExceptionHandler 之前
///
@Order(Ordered.LOWEST_PRECEDENCE - 1)
@Slf4j
@RestControllerAdvice
public class SaExceptionHandler {

    /// 未登录返回401
    @ExceptionHandler(NotLoginException.class)
    public ResponseEntity<Result<Void>> handleNotLoginException(NotLoginException ex){
        // 日志固定输出 messageKey 与中文翻译, 不受请求语言影响
        String key = ex.resolveMessageKey();
        log.info("鉴权异常 key={}, 消息={}", key, I18nUtil.get(key, Locale.CHINA, ex.getArgs()), ex);
        Result<Void> result = Res.response(ex.getCode(), ex.getMessage(), MDC.get(CommonCode.TRACE_ID));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
    }

    /// 路径无权访问
    @ExceptionHandler(RouterCheckException.class)
    public ResponseEntity<Result<Void>> handleBusinessException(RouterCheckException ex) {
        // 日志固定输出 messageKey 与中文翻译, 不受请求语言影响
        String key = ex.resolveMessageKey();
        log.info("鉴权异常 key={}, 消息={}", key, I18nUtil.get(key, Locale.CHINA, ex.getArgs()), ex);
        Result<Void> result = Res.response(ex.getCode(), ex.getMessage(), MDC.get(CommonCode.TRACE_ID));
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(result);
    }

    /// sa鉴权业务异常
    ///
    /// 注意: SSE 等异步端点产生的事件流响应 Content-Type 已锁定为 text/event-stream,
    /// 无 JSON 消息转换器, 此时返回 Result 会抛 HttpMessageNotWritableException, 故仅返回无 body 的状态码.
    @ExceptionHandler(SaTokenException.class)
    public ResponseEntity<?> handleBusinessException(SaTokenException ex, HttpServletResponse response) {
        log.info(ex.getMessage(), ex);
        String contentType = response.getContentType();
        // SSE 事件流响应: 无 JSON 转换器, 不写 body, 避免二次异常
        if (contentType != null && contentType.contains(MediaType.TEXT_EVENT_STREAM_VALUE)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        Result<Void> result = Res.response(CommonCode.FAIL_CODE, ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(result);
    }

}
