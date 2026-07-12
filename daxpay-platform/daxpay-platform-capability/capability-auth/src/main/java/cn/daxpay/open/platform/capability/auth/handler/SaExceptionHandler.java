package cn.daxpay.open.platform.capability.auth.handler;

import cn.daxpay.open.platform.common.i18n.util.I18nUtil;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
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
        log.info("鉴权异常 消息={}, key={}", I18nUtil.get(key, Locale.CHINA, ex.getArgs()), key, ex);
        Result<Void> result = Res.response(ex.getCode(), ex.getMessage(), MDC.get(CommonCode.TRACE_ID));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
    }

    /// 处理 Sa-Token 抛出的未登录异常(被踢下线/顶下线/过期等)
    ///
    /// Sa-Token 自身的 [cn.dev33.satoken.exception.NotLoginException] 与本项目自定义的
    /// [NotLoginException] 同名但不同包, 互无继承关系; 前者继承自 [SaTokenException],
    /// 若不在此单独处理, 会落入下方 [SaTokenException] 兜底而返回 500, 前端 401 拦截无法感知。
    /// 按异常 type 映射不同 i18n 文案, 返回 401 以便前端识别并跳转登录页。
    @ExceptionHandler(cn.dev33.satoken.exception.NotLoginException.class)
    public ResponseEntity<Result<Void>> handleSaTokenNotLoginException(cn.dev33.satoken.exception.NotLoginException ex) {
        String type = ex.getType();
        // 按未登录原因映射 i18n 消息 key
        String messageKey = switch (type) {
            case cn.dev33.satoken.exception.NotLoginException.NOT_TOKEN      -> "error.auth.notToken";
            case cn.dev33.satoken.exception.NotLoginException.INVALID_TOKEN  -> "error.auth.invalidToken";
            case cn.dev33.satoken.exception.NotLoginException.TOKEN_TIMEOUT  -> "error.auth.tokenTimeout";
            case cn.dev33.satoken.exception.NotLoginException.BE_REPLACED    -> "error.auth.beReplaced";
            case cn.dev33.satoken.exception.NotLoginException.KICK_OUT       -> "error.auth.kickOut";
            case cn.dev33.satoken.exception.NotLoginException.TOKEN_FREEZE   -> "error.auth.tokenFreeze";
            default -> "error.auth.notLogin";
        };
        String message = I18nUtil.get(messageKey);
        log.info("Sa-Token 未登录 type={}, key={}", type, messageKey);
        Result<Void> result = Res.response(CommonErrorCode.AUTHENTICATION_FAIL, message, MDC.get(CommonCode.TRACE_ID));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
    }

    /// 路径无权访问
    @ExceptionHandler(RouterCheckException.class)
    public ResponseEntity<Result<Void>> handleBusinessException(RouterCheckException ex) {
        // 日志固定输出 messageKey 与中文翻译, 不受请求语言影响
        String key = ex.resolveMessageKey();
        log.info("鉴权异常 消息={}, key={}", I18nUtil.get(key, Locale.CHINA, ex.getArgs()), key, ex);
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
